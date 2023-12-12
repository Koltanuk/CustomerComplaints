package com.complaints.controller;

import com.complaints.entity.CustomerComplaint;
import com.complaints.entity.ComplaintResponse;
import com.complaints.service.ComplaintService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/complaints")
@Slf4j
public class ComplaintController {
    private final ComplaintService complaintService;

    @Autowired
    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @PostMapping
    public CompletableFuture<ResponseEntity<CustomerComplaint>> createComplaint(@RequestBody CustomerComplaint customerComplaint) {
        if (customerComplaint.getUserId() == null) {
            log.error("Incoming userId is null");
            return CompletableFuture.completedFuture(ResponseEntity.badRequest().body(null));
        }
        return complaintService.createComplaint(customerComplaint)
                .thenApply(createdComplaint -> ResponseEntity.ok(createdComplaint))
                .exceptionally(ex -> {
                    log.error("Error creating complaint", ex);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }

    @GetMapping("/{id}")
    public CompletableFuture<ResponseEntity<ComplaintResponse>> getComplaintById(@PathVariable("id") UUID complaintId) {
        return complaintService.getComplaintById(complaintId)
                .thenApply(complaintResponseOpt -> complaintResponseOpt
                        .map(ResponseEntity::ok)
                        .orElseGet(() -> {
                            log.error("Complaint not found with ID: {}", complaintId);
                            return ResponseEntity.notFound().build();
                        }))
                .exceptionally(ex -> {
                    log.error("Error retrieving complaint with ID: " + complaintId, ex);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }
}
