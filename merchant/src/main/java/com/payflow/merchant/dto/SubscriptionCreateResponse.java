package com.payflow.merchant.dto;

import java.time.OffsetDateTime;

public record SubscriptionCreateResponse(Long subscriptionId, String status, OffsetDateTime currentPeriodEnd) {}
