package com.payflow.pg.domain;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(name = "provider", columnDefinition = "ENUM('MOCK','KAKAOPAY','TOSS')")
    private PaymentProvider provider;

    @Column(name = "pg_tid")
    private String pgTid;

    @Enumerated(EnumType.STRING)
    private PaymentState status;

    private LocalDateTime approvedAt;

    @Column(name = "signature_valid")
    private Boolean signatureValid;

    @Column(columnDefinition = "json")
    private String rawPayload;

    @Column(name = "created_at", updatable = false, insertable = false)
    private LocalDateTime createdAt;
}
