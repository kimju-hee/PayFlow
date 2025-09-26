package com.payflow.pg.service;

import com.payflow.pg.domain.Payment;
import com.payflow.pg.domain.PaymentProvider;
import com.payflow.pg.domain.PaymentState;
import com.payflow.pg.dto.ApproveRequest;
import com.payflow.pg.dto.ApproveResponse;
import com.payflow.pg.dto.ReadyRequest;
import com.payflow.pg.dto.ReadyResponse;
import com.payflow.pg.dto.WebhookPayload;
import com.payflow.pg.infra.CardClient;
import com.payflow.pg.infra.WebhookClient;
import com.payflow.pg.repository.PaymentRepository;
import com.payflow.pg.support.ApiException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository repo;
    private final CardClient cardClient;
    private final WebhookClient webhookClient;

    @Transactional
    public ReadyResponse ready(ReadyRequest req) {
        String pgTid = UUID.randomUUID().toString();

        Payment p = new Payment();
        p.setPgTid(pgTid);
        p.setOrderId(Long.valueOf(req.getOrderNo()));
        p.setProvider(PaymentProvider.MOCK);
        p.setStatus(PaymentState.READY);
        p.setSignatureValid(false);
        repo.save(p);

        return new ReadyResponse(pgTid, "http://localhost:3000/checkout/" + pgTid);
    }


    @Transactional
    public ApproveResponse approve(ApproveRequest req) {
        Payment p = repo.findByPgTid(req.getTid())
                .orElseThrow(() -> new ApiException(404, "TID_NOT_FOUND"));

        try {
            Map<String, Object> auth = cardClient.authorize(p.getPgTid(), req.getAmount(), req.getCardNoMasked());
            if (!"APPROVED".equals(String.valueOf(auth.get("result")))) {
                throw new ApiException(402, "AUTH_FAILED");
            }

            Map<String, Object> cap = cardClient.capture(p.getPgTid(), req.getAmount());
            if (!"APPROVED".equals(String.valueOf(cap.get("result")))) {
                throw new ApiException(500, "CAPTURE_FAILED");
            }

            p.setStatus(PaymentState.APPROVED);
            repo.save(p);

            webhookClient.send(new WebhookPayload(
                    p.getPgTid(), String.valueOf(p.getOrderId()), req.getAmount(), p.getStatus().name()
            ));

            return new ApproveResponse(p.getPgTid(), p.getStatus().name());
        } catch (ApiException e) {
            p.setStatus(PaymentState.FAILED);
            repo.save(p);

            webhookClient.send(new WebhookPayload(
                    p.getPgTid(), String.valueOf(p.getOrderId()), req.getAmount(), p.getStatus().name()
            ));

            throw e;
        }
    }
}
