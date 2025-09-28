package com.payflow.merchant.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String orderNo;
    @Column(nullable = false)
    private BigDecimal amount;
    @Column(nullable = false)
    private String state;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderItem> items = new ArrayList<>();
    @PrePersist void p1() { createdAt = OffsetDateTime.now(); updatedAt = createdAt; }
    @PreUpdate void p2() { updatedAt = OffsetDateTime.now(); }
}
