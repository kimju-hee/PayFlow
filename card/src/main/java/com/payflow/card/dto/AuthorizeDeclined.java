package com.payflow.card.dto;

public record AuthorizeDeclined(String result, String reason) {
    public AuthorizeDeclined(String reason) { this("DECLINED", reason); }
}