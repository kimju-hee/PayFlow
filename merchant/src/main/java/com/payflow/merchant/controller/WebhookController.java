package com.payflow.merchant.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.merchant.domain.MerchantPayment;
import com.payflow.merchant.dto.PgWebhookPayload;
import com.payflow.merchant.repository.PaymentRepository;
import com.payflow.merchant.util.HmacSigner;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/webhooks/pg")
public class WebhookController {
    private final PaymentRepository pays;
    private final ObjectMapper om = new ObjectMapper();
    @Value("${payflow.webhook.secret}") private String secret;

    @PostMapping("/payments")
    @Transactional
    public void onPayment(@RequestBody PgWebhookPayload payload, @RequestHeader("X-Payflow-Signature") String sig) {
        String body = toJson(payload);
        String expect = HmacSigner.sign(secret, body);
        if (!expect.equals(sig)) throw new IllegalArgumentException("invalid_signature");
        MerchantPayment mp = pays.findByOrderNo(payload.orderNo()).orElseGet(MerchantPayment::new);
        mp.setOrderNo(payload.orderNo());
        mp.setPgTid(payload.tid());
        mp.setState(payload.state());
        pays.save(mp);
    }

    private String toJson(Object o) {
        try { return om.writeValueAsString(o); } catch (Exception e) { throw new IllegalStateException(e); }
    }
}