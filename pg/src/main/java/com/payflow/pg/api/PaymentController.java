package com.payflow.pg.api;

import com.payflow.pg.dto.ApproveRequest;
import com.payflow.pg.dto.ApproveResponse;
import com.payflow.pg.dto.ReadyRequest;
import com.payflow.pg.dto.ReadyResponse;
import com.payflow.pg.dto.TransactionView;
import com.payflow.pg.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/pay")
public class PaymentController {
    private final PaymentService service;

    @PostMapping("/ready")
    public ReadyResponse ready(@RequestBody ReadyRequest req) {
        return service.ready(req);
    }

    @PostMapping("/approve")
    public ApproveResponse approve(@RequestBody ApproveRequest req) {
        return service.approve(req);
    }

    @GetMapping("/tx/{tid}")
    public TransactionView get(@PathVariable String tid) {
        return service.get(tid);
    }
}
