package com.payflow.pg.card;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
public class CardClient {
    private final RestClient rest;
    @Value("${payflow.card.baseUrl}") private String base;

    public AuthorizeRes authorize(String tid, BigDecimal amount, String merchantName) {
        return rest.post().uri(base + "/card/api/authorize")
                .contentType(MediaType.APPLICATION_JSON)
                .body(new AuthorizeReq(amount, merchantName, tid))
                .retrieve()
                .body(AuthorizeRes.class);
    }

    public record AuthorizeReq(BigDecimal amount, String merchantName, String tid) {}

    public record AuthorizeRes(String result, String refNo, String authCode, String approvedAt, String reason) {}
}
