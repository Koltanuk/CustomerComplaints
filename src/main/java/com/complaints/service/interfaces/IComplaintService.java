package com.complaints.service.interfaces;

import com.complaints.entity.CustomerComplaint;
import com.complaints.entity.ComplaintResponse;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public interface IComplaintService {
    CompletableFuture<CustomerComplaint> createComplaint(CustomerComplaint customerComplaint);
    CompletableFuture<Optional<ComplaintResponse>> getComplaintById(UUID complaintId);
}
