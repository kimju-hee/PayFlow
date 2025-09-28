package com.payflow.merchant.repository;

import com.payflow.merchant.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Optional<Order> findByOrderNo(String orderNo);
}
