package com.payflow.merchant.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
@Entity @Table(name = "invoices")
public class Invoice {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private Long orderId;
    @Column(nullable = false, unique = true)
    private String invoiceNo;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private OffsetDateTime issuedAt;
}
