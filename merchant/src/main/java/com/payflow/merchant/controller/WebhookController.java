package com.payflow.merchant.controller;

import com.payflow.merchant.dto.WebhookAck;
import com.payflow.merchant.service.WebhookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchant/api/payments")
public class WebhookController {
    private final WebhookService service;

    @PostMapping(value = "/webhook", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public WebhookAck handle(@RequestBody String rawBody,
                             @RequestHeader("X-Payflow-Signature") String sig) {
        service.handle(rawBody, sig);
        return new WebhookAck(true);
    }
}
