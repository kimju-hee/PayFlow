package com.payflow.merchant.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "subscriptions")
public class Subscription {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private String status;
    @Column(nullable = false)
    private OffsetDateTime currentPeriodEnd;
    private String customerToken;
}
