package com.complaints.service;

import com.complaints.entity.User;
import com.complaints.service.interfaces.IUserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.asynchttpclient.AsyncHttpClient;
import org.asynchttpclient.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
public class UserService implements IUserService {

    private static final String USER_SERVICE_URL = "http://localhost:8081/users/";
    private final AsyncHttpClient asyncHttpClient;
    private final ObjectMapper objectMapper;

    @Autowired
    public UserService(AsyncHttpClient asyncHttpClient, ObjectMapper objectMapper) {
        this.asyncHttpClient = asyncHttpClient;
        this.objectMapper = objectMapper;
    }

    public CompletableFuture<User> getUserById(UUID userId) {
        return asyncHttpClient
                .prepareGet(USER_SERVICE_URL + userId.toString())
                .execute()
                .toCompletableFuture()
                .thenApply(Response::getResponseBody)
                .thenApply(this::convertJsonToUser)
                .exceptionally(ex -> {
                    log.error("Error fetching user with ID " + userId, ex);
                    return null;
                });
    }

    private User convertJsonToUser(String json) {
        try {
            return objectMapper.readValue(json, User.class);
        } catch (Exception e) {
            log.error("Error parsing JSON to User object", e);
            throw new RuntimeException("Error in JSON conversion", e);
        }
    }


}