package com.matrimony.wallet.dto;

import com.matrimony.payment.dto.PaymentGateWay;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class WalletTopupRequest {
    @NotNull(message = "Amount cannot be null")
    @Positive(message = "Amount must be greater than 0")
    private BigDecimal amount;
    @NotBlank(message = "Gateway cannot be blank")
    private PaymentGateWay paymentGateway; // MOCK for now
}