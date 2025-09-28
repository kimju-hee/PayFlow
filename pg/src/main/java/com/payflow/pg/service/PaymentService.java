package com.payflow.pg.service;

import com.payflow.pg.dto.ReadyRequest;
import com.payflow.pg.dto.ReadyResponse;
import com.payflow.pg.dto.ApproveRequest;
import com.payflow.pg.dto.ApproveResponse;
import com.payflow.pg.dto.TransactionView;
import com.payflow.pg.dto.WebhookPayload;
import com.payflow.pg.card.CardClient;
import com.payflow.pg.domain.Payment;
import com.payflow.pg.dto.*;
import com.payflow.pg.repository.PaymentRepository;
import com.payflow.pg.util.HmacSigner;
import com.payflow.pg.util.Jsons;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {
    private final PaymentRepository repo;
    @Qualifier("cardHttpClient")
    private final CardClient card;    private final RestClient rest;

    @Value("${payflow.webhook.secret}") private String secret;

    @Transactional
    public ReadyResponse ready(ReadyRequest req) {
        String tid = "TID-" + UUID.randomUUID();
        Payment p = Payment.builder()
                .tid(tid)
                .pgTid(tid)
                .provider(Payment.Provider.MOCK)
                .status(Payment.Status.READY)
                .orderNo(req.orderNo())
                .amount(req.amount())
                .callbackUrl(req.callbackUrl())
                .state("READY")
                .signatureValid(false)
                .build();

        repo.save(p);
        String redirect = "https://pg.local/checkout?tid=" + tid;
        return new ReadyResponse(tid, redirect);
    }


    @Transactional
    public ApproveResponse approve(ApproveRequest req) {
        Payment p = repo.findByTid(req.tid())
                .orElseThrow();

        if (!"READY".equals(p.getState())) throw new IllegalStateException("invalid_state");

        var a = card.authorize(p.getTid(), p.getAmount(), "PayFlow-PG");

        if (!"APPROVED".equalsIgnoreCase(a.result())) {
            p.setState("FAILED");
            p.setStatus(Payment.Status.FAILED);
            p.setFailureCode(a.result());
            p.setCardAuthCode(null);
            p.setCardRefNo(null);
            repo.save(p);
            sendWebhook(p, "PAYMENT.FAILED");
            return new ApproveResponse(p.getTid(), p.getState(), null, null);
        }

        p.setState("APPROVED");
        p.setStatus(Payment.Status.APPROVED);
        p.setApprovedAt(OffsetDateTime.now().toLocalDateTime());
        p.setCardAuthCode(a.authCode());
        p.setCardRefNo(a.refNo());
        p.setCardTxId(a.refNo());
        repo.save(p);

        sendWebhook(p, "PAYMENT.APPROVED");
        return new ApproveResponse(p.getTid(), p.getState(), p.getCardAuthCode(), p.getCardRefNo());
    }

    @Transactional(readOnly = true)
    public TransactionView get(String tid) {
        Payment p = repo.findByTid(tid)
                .orElseThrow();
        return new TransactionView(
                p.getTid(), p.getOrderNo(), p.getAmount(),
                p.getState(), p.getCardAuthCode(), p.getCardRefNo()
        );
    }

    private void sendWebhook(Payment p, String event) {
        log.info("Sending webhook to [{}]", p.getCallbackUrl());
        if (p.getCallbackUrl() == null || p.getCallbackUrl().isBlank()) return;

        var payload = new WebhookPayload(
                p.getTid(),
                p.getOrderNo(),
                p.getAmount().longValueExact(),
                p.getState()
        );
        String body = Jsons.toJson(payload);
        String sig  = HmacSigner.sign(secret, body);

        rest.post().uri(p.getCallbackUrl())
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Payflow-Signature", sig)
                .body(body)
                .retrieve()
                .toBodilessEntity();
    }

}
