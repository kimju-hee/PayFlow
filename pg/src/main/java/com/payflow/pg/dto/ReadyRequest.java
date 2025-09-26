package com.payflow.pg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReadyRequest {
    @NotBlank
    private String orderNo;
    @NotNull
    private Long amount;
    @NotBlank
    private String returnUrl;
}
