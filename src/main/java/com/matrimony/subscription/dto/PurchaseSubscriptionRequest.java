package com.matrimony.subscription.dto;

import com.matrimony.payment.dto.PaymentGateWay;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseSubscriptionRequest {
    @NotBlank(message = "UserId cannot be blank")
    private String userId;
    @NotBlank(message = "PlanId cannot be blank")
    private Long planId;
    @NotBlank(message = "Gateway cannot be blank")
    private PaymentGateWay paymentGateway; // RAZORPAY / GPAY (for now "MOCK")
}