package com.payflow.pg.dto;

public record WebhookPayload(
        String tid,
        String orderNo,
        Long amount,
        String state
) {}
