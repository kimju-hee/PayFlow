package com.payflow.pg.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveResponse {
    private String tid;
    private String state;

    public ApproveResponse(String tid, String state) {
        this.tid = tid;
        this.state = state;
    }}
