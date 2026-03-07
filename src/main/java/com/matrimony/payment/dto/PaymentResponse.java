package com.matrimony.payment.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    private String userId;
    @NotNull(message = "PaymentId cannot be blank")
    private Long paymentId;
    @NotBlank(message = "TransactionId cannot be blank")
    private String transactionId;
}