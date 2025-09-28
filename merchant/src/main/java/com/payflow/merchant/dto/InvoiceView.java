package com.payflow.merchant.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record InvoiceView(Long orderId, String invoiceNo, BigDecimal amount, OffsetDateTime issuedAt) {}
