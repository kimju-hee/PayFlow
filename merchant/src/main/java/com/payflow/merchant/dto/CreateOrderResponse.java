package com.payflow.merchant.dto;

import java.math.BigDecimal;

public record CreateOrderResponse(Long orderId, String orderNo, BigDecimal totalAmount, String status) {}