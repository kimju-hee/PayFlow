package com.payflow.merchant.dto;

import java.math.BigDecimal;
import java.util.List;

public record GetOrderResponse(Long orderId, String status, BigDecimal totalAmount, List<OrderItemView> items, GetOrderPayment payment) {}
