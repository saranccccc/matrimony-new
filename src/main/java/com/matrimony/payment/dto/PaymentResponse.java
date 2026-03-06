package com.matrimony.payment.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PaymentResponse {
    @NotBlank(message = "UserId cannot be blank")
    private String userId;
    @NotBlank(message = "PaymentId cannot be blank")
    private Long paymentId;
    @NotBlank(message = "TransactionId cannot be blank")
    private String transactionId;
}