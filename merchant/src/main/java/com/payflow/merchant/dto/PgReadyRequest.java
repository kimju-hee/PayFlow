package com.payflow.merchant.dto;

import java.math.BigDecimal;

public record PgReadyRequest(String orderNo, BigDecimal amount, String returnUrl) {}
