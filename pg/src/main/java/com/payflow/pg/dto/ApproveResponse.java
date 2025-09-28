package com.payflow.pg.dto;

public record ApproveResponse(String tid, String status, String cardAuthCode, String cardRefNo) {}
