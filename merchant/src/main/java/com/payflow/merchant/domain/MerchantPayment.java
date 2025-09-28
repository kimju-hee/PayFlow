package com.payflow.merchant.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "merchant_payments")
public class MerchantPayment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String orderNo;
    private String tid;
    private String state;
    private String redirectUrl;
    private String pgTid;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    @PrePersist void p1() { createdAt = OffsetDateTime.now(); updatedAt = createdAt; }
    @PreUpdate void p2() { updatedAt = OffsetDateTime.now(); }
}
