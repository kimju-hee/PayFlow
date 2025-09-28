package com.payflow.merchant.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.payflow.merchant.domain.Invoice;
import com.payflow.merchant.domain.MerchantPayment;
import com.payflow.merchant.dto.PgWebhookPayload;
import com.payflow.merchant.repository.InvoiceRepository;
import com.payflow.merchant.repository.PaymentRepository;
import com.payflow.merchant.util.HmacSigner;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;

@Service
@RequiredArgsConstructor
public class WebhookService {
    private final PaymentRepository pays;
    private final InvoiceRepository invoices;
    private final StringRedisTemplate redis;
    private final ObjectMapper om = new ObjectMapper();

    @Value("${payflow.webhook.secret}")
    private String secret;

    @Transactional
    public void handle(String rawBody, String signature) {
        String expect = HmacSigner.sign(secret, rawBody);
        if (!expect.equals(signature)) throw new IllegalArgumentException("invalid_signature");

        PgWebhookPayload payload;
        try { payload = om.readValue(rawBody, PgWebhookPayload.class); }
        catch (Exception e) { throw new IllegalArgumentException("invalid_payload"); }

        String dedupKey = "wh:merchant:" + payload.event() + ":" + payload.tid();
        Boolean ok = redis.opsForValue().setIfAbsent(dedupKey, "1");
        if (Boolean.FALSE.equals(ok)) return;

        if ("PAYMENT.APPROVED".equals(payload.event())) {
            MerchantPayment mp = pays.findByOrderNo(payload.orderNo()).orElseGet(MerchantPayment::new);
            mp.setOrderNo(payload.orderNo());
            mp.setPgTid(payload.tid());
            mp.setState("APPROVED");
            pays.save(mp);

            Long orderId = extractOrderIdFromOrderNo(payload.orderNo());
            invoices.findByOrderId(orderId).orElseGet(() -> {
                Invoice inv = Invoice.builder()
                        .orderId(orderId)
                        .invoiceNo(genInvoiceNo(orderId))
                        .amount(payload.amount())
                        .issuedAt(parseOrNow(payload.approvedAt()))
                        .build();
                return invoices.save(inv);
            });
        }
    }

    private String genInvoiceNo(Long orderId) { return "INV-" + String.format("%06d", orderId); }

    private OffsetDateTime parseOrNow(String s) {
        try { return OffsetDateTime.parse(s); } catch (DateTimeParseException e) { return OffsetDateTime.now(); }
    }

    private Long extractOrderIdFromOrderNo(String orderNo) {
        try { return Long.parseLong(orderNo.substring(orderNo.lastIndexOf('-') + 1)); }
        catch (Exception e) { throw new IllegalStateException("order_id_parse_error"); }
    }
}
