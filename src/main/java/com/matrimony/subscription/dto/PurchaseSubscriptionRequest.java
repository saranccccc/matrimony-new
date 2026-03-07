package com.matrimony.subscription.dto;

import com.matrimony.payment.dto.PaymentGateWay;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PurchaseSubscriptionRequest {
    private String userId;
    @NotNull(message = "PlanId cannot be blank")
    private Long planId;
    @NotNull(message = "Gateway cannot be blank")
    private PaymentGateWay paymentGateway; // RAZORPAY / GPAY (for now "MOCK")
}