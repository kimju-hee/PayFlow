package com.payflow.merchant.service;

import com.payflow.merchant.domain.MerchantPayment;
import com.payflow.merchant.domain.Order;
import com.payflow.merchant.repository.OrderRepository;
import com.payflow.merchant.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orders;
    private final PaymentRepository pays;
    private final RestClient rest;

    @Value("${pg.base-url:http://localhost:8082}")
    private String pgBase;

    @Value("${payflow.webhook.merchantEndpoint:http://localhost:8080/merchant/api/payments/webhook}")
    private String callbackUrl;

    @Transactional
    public ReadyPaymentResponse ready(ReadyPaymentRequest req) {
        Order o = orders.findById(req.orderId()).orElseThrow();
        var pgResp = rest.post()
                .uri(pgBase + "/pg/api/payments/ready")
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "orderNo", o.getOrderNo(),
                        "amount",  o.getAmount(),
                        "callbackUrl", callbackUrl
                ))
                .retrieve().body(PgReadyResponse.class);

        MerchantPayment mp = pays.findByOrderNo(o.getOrderNo()).orElseGet(MerchantPayment::new);
        mp.setOrderNo(o.getOrderNo());
        mp.setPgTid(pgResp.tid());
        mp.setRedirectUrl(pgResp.redirectUrl());
        mp.setState("READY");
        pays.save(mp);

        return new ReadyPaymentResponse(mp.getId(), pgResp.tid(), pgResp.redirectUrl());
    }

    public record PgReadyResponse(String tid, String redirectUrl) {}
    public record ReadyPaymentRequest(Long orderId, String provider) {}
    public record ReadyPaymentResponse(Long paymentId, String pgTid, String redirectUrl) {}
}
