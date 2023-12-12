package com.complaints;

import com.complaints.entity.*;
import com.complaints.repo.ComplaintRepository;
import com.complaints.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ComplaintServiceTest {

    @Mock
    private ComplaintRepository complaintRepository;

    @Mock
    private UserService userService;

    @Mock
    private PurchaseService purchaseService;

    private ComplaintService complaintService;

    private final UUID userId = UUID.fromString("a93adc57-4d59-4a9d-85c6-b5d48d99101d");
    private final UUID purchaseId = UUID.fromString("f256c996-6dcb-40cf-8dce-a11fa9bcab6b");

    private final UUID complaintId = UUID.fromString("6ae5e6e0-f2c4-4376-9066-cf2a598be882");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        complaintService = new ComplaintService(complaintRepository, userService, purchaseService);
    }

    @Test
    void testCreateComplaint() {

        CustomerComplaint customerComplaint = new CustomerComplaint(userId, "I didn't get an item", "Lorem ipsum dolor sit amet.", purchaseId);
        when(complaintRepository.save(any(CustomerComplaint.class))).thenReturn(customerComplaint);

        complaintService.createComplaint(customerComplaint).thenAccept(result -> {
            assertNotNull(result);
            assertEquals(customerComplaint, result);
        });

        verify(complaintRepository).save(any(CustomerComplaint.class));
    }

    @Test
    void testGetComplaintByIdFound() {
        UUID complaintId = UUID.randomUUID();
        CustomerComplaint customerComplaint = new CustomerComplaint(userId, "I didn't get an item", "Lorem ipsum dolor sit amet.", purchaseId);
        customerComplaint.setId(complaintId);
        User user = new User(); // Mock user data
        Purchase purchase = new Purchase(); // Mock purchase data

        when(complaintRepository.findById(complaintId)).thenReturn(Optional.of(customerComplaint));
        when(userService.getUserById(userId)).thenReturn(CompletableFuture.completedFuture(user));
        when(purchaseService.getPurchaseById(purchaseId)).thenReturn(CompletableFuture.completedFuture(purchase));

        complaintService.getComplaintById(complaintId).thenAccept(result -> {
            assertTrue(result.isPresent());
            assertEquals(customerComplaint, result.get().getCustomerComplaint());
            assertEquals(user, result.get().getUser());
            assertEquals(purchase, result.get().getPurchase());
        });

        verify(complaintRepository).findById(complaintId);
        verify(userService).getUserById(any(UUID.class));
        verify(purchaseService).getPurchaseById(any(UUID.class));
    }

    @Test
    void testGetComplaintByIdNotFound() {
        UUID complaintId = UUID.randomUUID();

        when(complaintRepository.findById(complaintId)).thenReturn(Optional.empty());

        complaintService.getComplaintById(complaintId).thenAccept(result -> {
            assertFalse(result.isPresent());
        });

        verify(complaintRepository).findById(complaintId);
        verify(userService, never()).getUserById(any());
        verify(purchaseService, never()).getPurchaseById(any());
    }
}