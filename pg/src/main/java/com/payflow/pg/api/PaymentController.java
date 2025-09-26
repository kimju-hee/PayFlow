package com.payflow.pg.api;

import com.payflow.pg.dto.ApproveRequest;
import com.payflow.pg.dto.ApproveResponse;
import com.payflow.pg.dto.ReadyRequest;
import com.payflow.pg.dto.ReadyResponse;
import com.payflow.pg.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentService service;

    public PaymentController(PaymentService service) {
        this.service = service;
    }

    @Operation(summary = "결제 준비", description = "TID 생성 및 체크아웃 URL 반환")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/ready")
    public ReadyResponse ready(@Valid @RequestBody ReadyRequest req) {
        return service.ready(req);
    }

    @Operation(summary = "결제 승인", description = "카드사 승인 후 결과 반환 및 웹훅 발송")
    @ApiResponse(responseCode = "200", description = "성공")
    @PostMapping("/approve")
    public ApproveResponse approve(@Valid @RequestBody ApproveRequest req) {
        return service.approve(req);
    }
}
