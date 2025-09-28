package com.payflow.pg.dto;

public record ApproveRequest(String tid, Card card) {
    public record Card(String number, String expiry, String cvc) {}
}
