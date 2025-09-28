package com.payflow.merchant.dto;

import java.time.OffsetDateTime;

public record SubscriptionRenewResponse(String status, OffsetDateTime currentPeriodEnd) {}
