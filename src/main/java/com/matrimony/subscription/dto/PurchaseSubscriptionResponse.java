package com.matrimony.subscription.dto;

import com.matrimony.payment.dto.PaymentGateWay;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PurchaseSubscriptionResponse {
    private Long paymentId;
    private BigDecimal amount;
    private PaymentGateWay paymentGateway;
    private String message; // next steps
}
