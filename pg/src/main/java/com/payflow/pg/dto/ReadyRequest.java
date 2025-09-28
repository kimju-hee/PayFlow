package com.payflow.pg.dto;

import java.math.BigDecimal;

public record ReadyRequest(
        String orderNo,
        BigDecimal amount,
        String callbackUrl
) {}
