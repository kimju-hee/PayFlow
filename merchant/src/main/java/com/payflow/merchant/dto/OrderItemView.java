package com.payflow.merchant.dto;

import java.math.BigDecimal;

public record OrderItemView(Long productId, Integer quantity, BigDecimal unitPrice) {}
