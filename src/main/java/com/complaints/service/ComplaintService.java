package com.complaints.service;

import com.complaints.entity.CustomerComplaint;
import com.complaints.entity.ComplaintResponse;
import com.complaints.entity.Purchase;
import com.complaints.entity.User;
import com.complaints.repo.ComplaintRepository;
import com.complaints.service.interfaces.IComplaintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
public class ComplaintService implements IComplaintService {

    private final ComplaintRepository complaintRepository;
    private final UserService userService;
    private final PurchaseService purchaseService;

    @Autowired
    public ComplaintService(ComplaintRepository complaintRepository,
                            UserService userService,
                            PurchaseService purchaseService) {
        this.complaintRepository = complaintRepository;
        this.userService = userService;
        this.purchaseService = purchaseService;
    }

    public CompletableFuture<CustomerComplaint> createComplaint(CustomerComplaint customerComplaint) {
        return CompletableFuture.supplyAsync(() -> {
            log.info("Creating complaint for user ID: {}", customerComplaint.getUserId());
            try {
                return complaintRepository.save(customerComplaint);
            } catch (Exception e) {
                log.error("Error while saving complaint for user ID: {}", customerComplaint.getUserId(), e);
                throw e;
            }
        });
    }

    public CompletableFuture<Optional<ComplaintResponse>> getComplaintById(UUID complaintId) {
        log.info("Starting complaint service getComplaintById for id {}", complaintId);

        return CompletableFuture.supplyAsync(() -> complaintRepository.findById(complaintId))
                .thenCompose(complaintOptional -> complaintOptional
                        .map(complaint -> {
                            CompletableFuture<User> userFuture = userService.getUserById(complaint.getUserId());
                            log.info("we get User data for user id {}", userFuture.join().getId());
                            CompletableFuture<Purchase> purchaseFuture =
                                    complaint.getPurchaseId() != null ?
                                            purchaseService.getPurchaseById(complaint.getPurchaseId()) :
                                            CompletableFuture.completedFuture(null);

                            return CompletableFuture.allOf(userFuture, purchaseFuture)
                                    .thenApply(v -> {
                                        User user = userFuture.join();
                                        Purchase purchase = (purchaseFuture != null) ? purchaseFuture.join() : null;
                                        return Optional.of(new ComplaintResponse(complaint, user, purchase));
                                    });
                        })
                        .orElseGet(() -> {
                            log.error("Complaint not found with ID: {}", complaintId);
                            return CompletableFuture.completedFuture(Optional.empty());
                        }));
    }




}

