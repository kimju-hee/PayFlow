package com.payflow.merchant.controller;

import com.payflow.merchant.dto.*;
import com.payflow.merchant.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchant/api/subscriptions")
public class SubscriptionController {
    private final SubscriptionService subs;

    @PostMapping
    public SubscriptionCreateResponse create(@RequestBody SubscriptionCreateRequest req) { return subs.create(req); }

    @PostMapping("/renew")
    public SubscriptionRenewResponse renew(@RequestBody SubscriptionRenewRequest req) { return subs.renew(req); }
}
