package com.payflow.merchant.controller;

import com.payflow.merchant.dto.ProductsPage;
import com.payflow.merchant.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/merchant/api/products")
public class ProductController {
    private final ProductService products;

    @GetMapping
    public ProductsPage list(
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Boolean active,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return products.list(type, active, page, size);
    }
}
