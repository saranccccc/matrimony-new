package com.matrimony.payment.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class PaymentInitRequest {
    @NotBlank(message = "UserId cannot be blank")
    private String userId;
    private Long planId;
    @NotBlank(message = "PaymentPurpose cannot be blank")
    private PaymentPurpose paymentPurpose;
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;
    @NotNull(message = "Gateway cannot be blank")
    private PaymentGateWay gateway;
}