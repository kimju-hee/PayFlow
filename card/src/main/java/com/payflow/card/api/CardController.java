package com.payflow.card.api;

import com.payflow.card.dto.AuthorizeRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/card/api")
public class CardController {
    @PostMapping("/authorize")
    public AuthorizeRes authorize(@RequestBody AuthorizeReq req) {
        // 모의 승인 응답
        return new AuthorizeRes("APPROVED", "R123456", "A123456", "2025-09-29T00:00:00+09:00", null);
    }

    public record AuthorizeReq(BigDecimal amount, String merchantName, String tid) {}
    public record AuthorizeRes(String result, String refNo, String authCode, String approvedAt, String reason) {}
}