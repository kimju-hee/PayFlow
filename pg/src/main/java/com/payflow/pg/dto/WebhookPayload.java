package com.payflow.pg.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebhookPayload {
    private String tid;
    private String orderNo;
    private Long amount;
    private String state;
}
