package com.complaints.service.interfaces;

import com.complaints.entity.User;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IUserService {
    CompletableFuture<User> getUserById(UUID userId);
}
