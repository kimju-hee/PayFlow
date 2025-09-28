package com.payflow.merchant.dto;

public record PaymentView(Long paymentId, String orderNo, String pgTid, String state, String redirectUrl) {}
