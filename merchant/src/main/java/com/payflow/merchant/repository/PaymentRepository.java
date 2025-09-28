package com.payflow.merchant.repository;

import com.payflow.merchant.domain.MerchantPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<MerchantPayment, Long> {
    Optional<MerchantPayment> findByOrderNo(String orderNo);
}
