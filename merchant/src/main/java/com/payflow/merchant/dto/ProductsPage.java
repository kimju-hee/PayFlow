package com.payflow.merchant.dto;

import java.util.List;

public record ProductsPage(List<ProductView> content, int page, int size, long totalElements, int totalPages) {}
