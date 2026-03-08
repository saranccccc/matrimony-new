package com.matrimony.unlock.service;

import com.matrimony.payment.dto.PaymentResponse;
import com.matrimony.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class ContactUnlockOrchestrator {

    private final PaymentService paymentService;
    private final ContactUnlockService contactUnlockService;

   /* @Transactional
    public UnlockResponse initiateContactUnlock(String userId, Long targetProfileId) {
        PaymentResponse paymentResponse = paymentService.initiateContactUnlockPayment(userId, targetProfileId);

        if (paymentResponse.isSuccess()) {
            contactUnlockService.unlockContact(userId, targetProfileId, paymentResponse.getPaymentId());
        }

        return new UnlockResponse(paymentResponse.getPaymentId(), paymentResponse.getStatus());
    }*/
}