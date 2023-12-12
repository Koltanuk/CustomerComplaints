package com.complaints.service;

import com.complaints.entity.Purchase;
import com.complaints.service.interfaces.IPurchaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class PurchaseService implements IPurchaseService {

    private static final String PURCHASE_SERVICE_URL = "http://localhost:8081/purchases/";
    private final AsyncHttpClient asyncHttpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public PurchaseService(AsyncHttpClient asyncHttpClient, ObjectMapper objectMapper) {
        this.asyncHttpClient = asyncHttpClient;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<Purchase> getPurchaseById(UUID purchaseId) {
        log.info("Fetching purchase with ID: {}", purchaseId);
        return asyncHttpClient
                .prepareGet(PURCHASE_SERVICE_URL + purchaseId.toString())
                .execute()
                .toCompletableFuture()
                .thenApply(Response::getResponseBody)
                .thenApply(this::convertJsonToPurchase)
                .exceptionally(ex -> {
                    log.error("Error fetching purchase with ID: {}", purchaseId, ex);
                    return null;
                });
    }

    private Purchase convertJsonToPurchase(String json) {
        try {
            return objectMapper.readValue(json, Purchase.class);
        } catch (Exception e) {
            log.error("Error in converting JSON to Purchase object", e);
            throw new RuntimeException("Error in JSON conversion", e);
        }
    }

}
