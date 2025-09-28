package com.payflow.merchant.domain;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "payments")
public class Payment {
    @Id private String tid;
    private String orderNo;
    private BigDecimal amount;
    private String state;
    private String cardTxId;
    private String failureCode;
}
