package com.complaints;

import com.complaints.controller.ComplaintController;
import com.complaints.entity.CustomerComplaint;
import com.complaints.entity.ComplaintResponse;
import com.complaints.service.ComplaintService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;


import java.util.Optional;
import java.util.UUID;

import java.util.concurrent.CompletableFuture;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

class ComplaintControllerTest {

    @Mock
    private ComplaintService complaintService;

    @InjectMocks
    private ComplaintController complaintController;

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    private final UUID userId = UUID.fromString("a93adc57-4d59-4a9d-85c6-b5d48d99101d");
    private final UUID purchaseId = UUID.fromString("f256c996-6dcb-40cf-8dce-a11fa9bcab6b");
    private final UUID complaintId = UUID.fromString("6ae5e6e0-f2c4-4376-9066-cf2a598be882");

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(complaintController).build();
    }

    @Test
    void testCreateComplaint() throws Exception {
        CustomerComplaint customerComplaint = new CustomerComplaint(userId, "I didn't get an item", "Lorem ipsum dolor sit amet.", purchaseId);
        when(complaintService.createComplaint(any(CustomerComplaint.class))).thenReturn(CompletableFuture.completedFuture(customerComplaint));

        String complaintJson = objectMapper.writeValueAsString(customerComplaint);

        mockMvc.perform(post("/complaints")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(complaintJson))
                .andExpect(status().isOk());

        verify(complaintService).createComplaint(any(CustomerComplaint.class));
    }



    @Test
    void testGetComplaintById() throws Exception {
        ComplaintResponse complaintResponse = new ComplaintResponse();
        when(complaintService.getComplaintById(complaintId)).thenReturn(CompletableFuture.completedFuture(Optional.of(complaintResponse)));

        mockMvc.perform(get("/complaints/" + complaintId))
                .andExpect(status().isOk());

        verify(complaintService).getComplaintById(complaintId);
    }
}
