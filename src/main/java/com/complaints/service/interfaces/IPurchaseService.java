package com.complaints.service.interfaces;

import com.complaints.entity.Purchase;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IPurchaseService {
    CompletableFuture<Purchase> getPurchaseById(UUID purchaseId);
}
