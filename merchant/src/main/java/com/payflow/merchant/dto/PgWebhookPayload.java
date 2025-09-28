package com.payflow.merchant.dto;

import java.math.BigDecimal;

public record PgWebhookPayload(String tid, String orderNo, String state, BigDecimal amount, String approvedAt, String failureCode) {}