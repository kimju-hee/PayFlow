package com.payflow.card.dto;

public record AuthorizeApproved(String result, String refNo, String authCode, String approvedAt) {
    public AuthorizeApproved(String refNo, String authCode, String approvedAt) {
        this("APPROVED", refNo, authCode, approvedAt);
    }
}