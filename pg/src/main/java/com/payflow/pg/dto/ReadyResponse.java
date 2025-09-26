package com.payflow.pg.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadyResponse {
    private String tid;
    private String checkoutUrl;

    public ReadyResponse(String tid, String checkoutUrl) {
        this.tid = tid;
        this.checkoutUrl = checkoutUrl;
    }
}
