package com.payflow.merchant.service;

import com.payflow.merchant.domain.MerchantPayment;
import com.payflow.merchant.domain.Order;
import com.payflow.merchant.dto.PgReadyRequest;
import com.payflow.merchant.dto.PgReadyResponse;
import com.payflow.merchant.dto.ReadyPaymentRequest;
import com.payflow.merchant.dto.ReadyPaymentResponse;
import com.payflow.merchant.repository.OrderRepository;
import com.payflow.merchant.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

@Service
@RequiredArgsConstructor
public class PaymentService {
    private final OrderRepository orders;
    private final PaymentRepository pays;
    private final RestClient rest;
    @Value("${payflow.pg.readyUrl:http://localhost:8082/payments/ready}")
    private String pgReadyUrl;

    @Transactional
    public ReadyPaymentResponse ready(ReadyPaymentRequest req) {
        Order o = orders.findById(req.orderId()).orElseThrow();
        PgReadyResponse pg = rest.post()
                .uri(pgReadyUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .body(new PgReadyRequest(o.getOrderNo(), o.getAmount(), "http://localhost:8081/return"))
                .retrieve()
                .body(PgReadyResponse.class);
        MerchantPayment mp = pays.findByOrderNo(o.getOrderNo()).orElseGet(MerchantPayment::new);
        mp.setOrderNo(o.getOrderNo());
        mp.setPgTid(pg.tid());
        mp.setRedirectUrl(pg.checkoutUrl());
        mp.setState("READY");
        pays.save(mp);
        return new ReadyPaymentResponse(mp.getId(), pg.tid(), pg.checkoutUrl());
    }
}
