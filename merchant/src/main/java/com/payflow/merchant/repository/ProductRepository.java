package com.payflow.merchant.repository;

import com.payflow.merchant.domain.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findByTypeAndActive(String type, boolean active, Pageable pageable);
    Page<Product> findByType(String type, Pageable pageable);
    Page<Product> findByActive(boolean active, Pageable pageable);
}
