package com.payflow.merchant.service;

import com.payflow.merchant.domain.Subscription;
import com.payflow.merchant.dto.*;
import com.payflow.merchant.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;

@Service
@RequiredArgsConstructor
public class SubscriptionService {
    private final SubscriptionRepository subs;

    @Transactional
    public SubscriptionCreateResponse create(SubscriptionCreateRequest req) {
        Subscription s = Subscription.builder()
                .productId(req.productId())
                .status("ACTIVE")
                .currentPeriodEnd(OffsetDateTime.now().plusMonths(1))
                .customerToken("tok_" + req.productId())
                .build();
        subs.save(s);
        return new SubscriptionCreateResponse(s.getId(), s.getStatus(), s.getCurrentPeriodEnd());
    }

    @Transactional
    public SubscriptionRenewResponse renew(SubscriptionRenewRequest req) {
        Subscription s = subs.findById(req.subscriptionId()).orElseThrow(() -> new IllegalStateException("subscription_not_found"));
        s.setCurrentPeriodEnd(s.getCurrentPeriodEnd().isAfter(OffsetDateTime.now())
                ? s.getCurrentPeriodEnd().plusMonths(1)
                : OffsetDateTime.now().plusMonths(1));
        s.setStatus("ACTIVE");
        subs.save(s);
        return new SubscriptionRenewResponse(s.getStatus(), s.getCurrentPeriodEnd());
    }
}
