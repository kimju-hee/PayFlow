package com.payflow.pg.dto;

import java.math.BigDecimal;

public record TransactionView(String tid, String orderNo, BigDecimal amount, String status,
                              String cardAuthCode, String cardRefNo) {}