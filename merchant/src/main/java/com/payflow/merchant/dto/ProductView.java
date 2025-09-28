package com.payflow.merchant.dto;

import java.math.BigDecimal;

public record ProductView(Long id, String name, BigDecimal price, String currency) {}
