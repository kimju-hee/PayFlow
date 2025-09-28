package com.payflow.merchant.controller;

import com.payflow.merchant.dto.CreateOrderRequest;
import com.payflow.merchant.dto.CreateOrderResponse;
import com.payflow.merchant.dto.GetOrderResponse;
import com.payflow.merchant.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchant/api/orders")
public class OrderController {
    private final OrderService service;
    @PostMapping
    public CreateOrderResponse create(@RequestBody CreateOrderRequest req) { return service.create(req); }
    @GetMapping("/{orderId}")
    public GetOrderResponse get(@PathVariable Long orderId) { return service.get(orderId); }
}
