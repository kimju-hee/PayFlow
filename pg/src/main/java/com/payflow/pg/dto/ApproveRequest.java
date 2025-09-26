package com.payflow.pg.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApproveRequest {
    @NotBlank
    private String tid;
    @NotBlank
    private String cardNoMasked;
    @NotNull
    private Long amount;
}
