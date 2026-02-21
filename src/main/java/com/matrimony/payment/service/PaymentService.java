package com.matrimony.payment.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.payment.dto.PaymentStatus;
import com.matrimony.payment.entity.Payment;
import com.matrimony.payment.repository.PaymentRepository;
import com.matrimony.plan.entity.Plan;
import com.matrimony.plan.entity.PlanRepository;
import com.matrimony.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PlanRepository planRepository;
    private final SubscriptionService subscriptionService;

    public Payment createSubscriptionPayment(String userId, Long planId) {

        Plan plan = planRepository.findByIdAndActiveTrue(planId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));

        Payment payment = Payment.builder()
                .userId(userId)
                .planId(planId)
                .amount(plan.getPrice())
                .status(PaymentStatus.INITIATED)
                .purpose("SUBSCRIPTION")
                .build();

        return paymentRepository.save(payment);
    }

    @Transactional
    public void markPaymentSuccess(String transactionId) {

        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));

        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return; // already processed
        }

        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        if ("SUBSCRIPTION".equals(payment.getPurpose())) {
            subscriptionService.purchaseSubscription(
                    payment.getUserId(),
                    payment.getPlanId()
            );
        }
    }
}


