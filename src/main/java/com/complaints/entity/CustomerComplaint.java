package com.complaints.entity;

import lombok.*;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Table(name = "complaints")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Type(type = "org.hibernate.type.UUIDBinaryType")
    private UUID id;

    @Column(nullable = false)
    @Type(type = "org.hibernate.type.UUIDBinaryType")
    private UUID userId;

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, length = 1000)
    private String complaint;

    @Column
    @Type(type = "org.hibernate.type.UUIDBinaryType")
    private UUID purchaseId;

    public CustomerComplaint(UUID userId, String subject, String complaint, UUID purchaseId) {
        this.userId = userId;
        this.subject = subject;
        this.complaint = complaint;
        this.purchaseId = purchaseId;
    }
}