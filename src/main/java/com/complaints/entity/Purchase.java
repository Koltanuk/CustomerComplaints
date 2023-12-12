package com.complaints.entity;

import lombok.*;

import java.util.Date;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Purchase {
    private UUID id;
    private UUID userId;
    private UUID productId;
    private String productName;
    private double pricePaidAmount;
    private String priceCurrency;
    private double discountPercent;
    private UUID merchantId;
    private Date purchaseDate;
}
