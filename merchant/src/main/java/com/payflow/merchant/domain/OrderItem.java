package com.payflow.merchant.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Integer quantity;
    @Column(nullable = false)
    private BigDecimal unitPrice;
    private OffsetDateTime createdAt;
    @PrePersist void p() { createdAt = OffsetDateTime.now(); }
}
