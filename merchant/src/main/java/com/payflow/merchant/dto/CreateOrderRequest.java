package com.payflow.merchant.dto;

import java.util.List;

public record CreateOrderRequest(List<CreateOrderItem> items, String currency) {}
