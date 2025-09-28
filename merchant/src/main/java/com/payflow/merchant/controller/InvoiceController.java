package com.payflow.merchant.controller;

import com.payflow.merchant.dto.InvoiceView;
import com.payflow.merchant.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchant/api/invoices")
public class InvoiceController {
    private final InvoiceService invoices;

    @GetMapping("/{orderId}")
    public InvoiceView get(@PathVariable Long orderId) { return invoices.getByOrderId(orderId); }
}
