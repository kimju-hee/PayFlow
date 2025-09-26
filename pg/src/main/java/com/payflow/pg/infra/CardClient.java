package com.payflow.pg.infra;

import org.springframework.stereotype.Component;
import java.util.Map;
import java.util.UUID;

@Component
public class CardClient {
    public Map<String, Object> authorize(String tid, Long amount, String cardNoMasked) {
        return Map.of("result", "APPROVED", "authCode", "A-" + UUID.randomUUID());
    }
    public Map<String, Object> capture(String tid, Long amount) {
        return Map.of("result", "APPROVED", "cardTxId", "TX-" + UUID.randomUUID());
    }
}
