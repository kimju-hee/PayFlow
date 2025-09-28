package com.payflow.merchant.service;

import com.payflow.merchant.domain.Product;
import com.payflow.merchant.dto.ProductView;
import com.payflow.merchant.dto.ProductsPage;
import com.payflow.merchant.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository products;

    @Transactional(readOnly = true)
    public ProductsPage list(String type, Boolean active, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        Page<Product> p;
        if (type != null && active != null) p = products.findByTypeAndActive(type, active, pageable);
        else if (type != null)              p = products.findByType(type, pageable);
        else if (active != null)            p = products.findByActive(active, pageable);
        else                                p = products.findAll(pageable);

        List<ProductView> content = p.getContent().stream()
                .map(it -> new ProductView(it.getId(), it.getName(), it.getPrice(), it.getCurrency()))
                .toList();

        return new ProductsPage(content, p.getNumber(), p.getSize(), p.getTotalElements(), p.getTotalPages());
    }
}
