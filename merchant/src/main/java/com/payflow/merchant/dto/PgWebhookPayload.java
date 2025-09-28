package com.payflow.merchant.dto;

import java.math.BigDecimal;

public record PgWebhookPayload(String event, String tid, String orderNo, BigDecimal amount, String approvedAt) {}
