package com.payflow.merchant.repository;

import com.payflow.merchant.domain.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {}
