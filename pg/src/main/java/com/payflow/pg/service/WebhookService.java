package com.payflow.pg.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.pg.domain.WebhookEvent;
import com.payflow.pg.repository.WebhookEventRepository;
import com.payflow.pg.util.HmacSigner;
import com.payflow.pg.util.Jsons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WebhookService {
    private final WebhookEventRepository repo;

    @Value("${payflow.webhook.secret:}")
    private String secret;

    public void handle(HttpHeaders httpHeaders, String body) {
        String sig = httpHeaders.getFirst("X-Payflow-Signature");
        boolean valid = false;
        if (secret != null && !secret.isBlank() && body != null) {
            String expected = HmacSigner.sign(secret, body);
            valid = expected.equals(sig);
        }

        String headersStr = httpHeaders.entrySet().stream()
                .map(e -> e.getKey() + ": " + String.join(",", e.getValue()))
                .collect(Collectors.joining("\n"));

        String tid = null, status = null, event = null;
        try {
            if (body != null && !body.isBlank()) {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode node = mapper.readTree(body);
                tid    = node.path("tid").asText(null);
                status = node.path("status").asText(null);
                if (status == null) status = node.path("state").asText(null);
                event  = node.path("event").asText(null);
            }
        } catch (Exception ignore) {}

        log.info("WEBHOOK RECEIVED (valid={}): sig={}, event={}, tid={}, status={}\nHeaders:\n{}\nBody:\n{}",
                valid, sig, event, tid, status, headersStr, body);

        WebhookEvent we = WebhookEvent.builder()
                .receivedAt(LocalDateTime.now())
                .signature(sig)
                .signatureValid(valid)
                .event(event)
                .tid(tid)
                .status(status)
                .headers(headersStr)
                .body(body)
                .build();
        repo.save(we);
    }
}