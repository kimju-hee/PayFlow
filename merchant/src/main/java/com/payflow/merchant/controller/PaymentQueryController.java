package com.payflow.merchant.controller;

import com.payflow.merchant.domain.MerchantPayment;
import com.payflow.merchant.dto.PaymentView;
import com.payflow.merchant.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchant/api/payments")
public class PaymentQueryController {
    private final PaymentRepository pays;

    @GetMapping("/{orderId}")
    public PaymentView get(@PathVariable Long orderId) {
        MerchantPayment mp = pays.findAll().stream().filter(p -> p.getOrderNo() != null && p.getOrderNo().endsWith("-" + orderId)).findFirst().orElse(null);
        if (mp == null) throw new IllegalStateException("payment_not_found");
        return new PaymentView(mp.getId(), mp.getOrderNo(), mp.getPgTid(), mp.getState(), mp.getRedirectUrl());
    }
}