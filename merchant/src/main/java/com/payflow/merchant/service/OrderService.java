package com.payflow.merchant.service;

import com.payflow.merchant.domain.Order;
import com.payflow.merchant.domain.OrderItem;
import com.payflow.merchant.dto.*;
import com.payflow.merchant.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orders;

    @Transactional
    public CreateOrderResponse create(CreateOrderRequest req) {
        Order o = Order.builder()
                .orderNo("O-" + java.time.LocalDate.now() + "-" + RandomStringUtils.randomNumeric(4))
                .state("CREATED")
                .build();
        BigDecimal total = BigDecimal.ZERO;
        for (CreateOrderItem ci : req.items()) {
            OrderItem it = OrderItem.builder()
                    .order(o)
                    .productId(ci.productId())
                    .quantity(ci.quantity())
                    .unitPrice(priceOf(ci.productId()))
                    .build();
            o.getItems().add(it);
            total = total.add(it.getUnitPrice().multiply(BigDecimal.valueOf(ci.quantity())));
        }
        o.setAmount(total);
        orders.save(o);
        return new CreateOrderResponse(o.getId(), o.getOrderNo(), o.getAmount(), o.getState());
    }

    @Transactional(readOnly = true)
    public GetOrderResponse get(Long orderId) {
        Order o = orders.findById(orderId).orElseThrow();
        List<OrderItemView> items = o.getItems().stream()
                .map(i -> new OrderItemView(i.getProductId(), i.getQuantity(), i.getUnitPrice()))
                .toList();
        GetOrderPayment pay = new GetOrderPayment(null, "READY".equals(o.getState()) ? "READY" : o.getState());
        return new GetOrderResponse(o.getId(), "PENDING".equals(o.getState()) ? "PENDING" : o.getState(), o.getAmount(), items, pay);
    }

    private BigDecimal priceOf(Long productId) { return BigDecimal.valueOf(9900); }
}
