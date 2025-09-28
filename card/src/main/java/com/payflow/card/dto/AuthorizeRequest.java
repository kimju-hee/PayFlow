package com.payflow.card.dto;

import java.math.BigDecimal;

public record AuthorizeRequest(BigDecimal amount, String merchantName, String tid) {}
