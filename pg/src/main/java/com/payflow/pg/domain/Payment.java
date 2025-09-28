package com.payflow.pg.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments", indexes = {
        @Index(name = "uk_payments_tid", columnList = "pg_tid", unique = true)
})
public class Payment {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Provider provider;

    @Column(name = "pg_tid", length = 64, nullable = false)
    private String pgTid;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    private LocalDateTime approvedAt;

    @Column(nullable = false)
    private boolean signatureValid;

    @Column(columnDefinition = "json")
    private String rawPayload;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String tid;

    private BigDecimal amount;

    private String cardTxId;

    private String failureCode;

    @Column(name = "order_no")
    private String orderNo;

    private String state;

    @Column(name = "callback_url", length = 512)
    private String callbackUrl;

    @Column(name = "card_auth_code")
    private String cardAuthCode;

    @Column(name = "card_ref_no")
    private String cardRefNo;

    private LocalDateTime updatedAt;

    public enum Provider { MOCK, KAKAOPAY, TOSS }
    public enum Status { READY, APPROVED, FAILED, CANCELLED }
}
