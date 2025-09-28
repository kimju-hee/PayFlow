package com.payflow.merchant.service;

import com.payflow.merchant.domain.Invoice;
import com.payflow.merchant.dto.InvoiceView;
import com.payflow.merchant.repository.InvoiceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class InvoiceService {
    private final InvoiceRepository invoices;

    @Transactional(readOnly = true)
    public InvoiceView getByOrderId(Long orderId) {
        Invoice inv = invoices.findByOrderId(orderId).orElseThrow(() -> new IllegalStateException("invoice_not_found"));
        return new InvoiceView(inv.getOrderId(), inv.getInvoiceNo(), inv.getAmount(), inv.getIssuedAt());
    }
}
