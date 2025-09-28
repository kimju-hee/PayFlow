package com.payflow.merchant.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MerchantWebhookController {
    @PostMapping("/webhook/payflow")
    public ResponseEntity<Void> receive(@RequestBody(required=false) String body) {
        System.out.println("MERCHANT WEBHOOK: " + body);
        return ResponseEntity.ok().build();
    }
}