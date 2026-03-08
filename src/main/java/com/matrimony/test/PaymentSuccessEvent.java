package com.matrimony.test;

import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class PaymentSuccessEvent {

    private final String paymentId;
    private final String referenceType;
    private final Long referenceId;


}