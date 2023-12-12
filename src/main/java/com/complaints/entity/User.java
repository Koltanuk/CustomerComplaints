package com.complaints.entity;

import lombok.*;

import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    private UUID id;
    private String fullName;
    private String emailAddress;
    private String physicalAddress;
}
