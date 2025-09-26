package com.payflow.pg.service;

import com.payflow.pg.domain.Payment;
import com.payflow.pg.domain.PaymentState;
import com.payflow.pg.dto.ReadyRequest;
import com.payflow.pg.dto.ReadyResponse;
import com.payflow.pg.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class PaymentService {
    private final PaymentRepository repo;

    public PaymentService(PaymentRepository repo) {
        this.repo = repo;
    }

    @Transactional
    public ReadyResponse ready(ReadyRequest req) {
        String tid = UUID.randomUUID().toString();

        Payment p = new Payment();
        p.setTid(tid);
        p.setOrderNo(req.getOrderNo());
        p.setAmount(req.getAmount());
        p.setState(PaymentState.READY);

        repo.save(p);

        return new ReadyResponse(tid, "http://localhost:3000/checkout/" + tid);
    }
}
