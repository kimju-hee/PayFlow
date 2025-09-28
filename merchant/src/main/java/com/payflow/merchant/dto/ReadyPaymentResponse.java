package com.payflow.merchant.dto;

public record ReadyPaymentResponse(Long paymentId, String pgTid, String redirectUrl) {}
