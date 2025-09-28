package com.payflow.merchant.dto;

public record ReadyPaymentRequest(Long orderId, String provider) {}
