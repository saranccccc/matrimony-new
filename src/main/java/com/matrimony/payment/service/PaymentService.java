package com.matrimony.payment.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.common.validator.AutoValidate;
import com.matrimony.payment.dto.PaymentInitRequest;
import com.matrimony.payment.dto.PaymentPurpose;
import com.matrimony.payment.dto.PaymentResponse;
import com.matrimony.payment.dto.PaymentStatus;
import com.matrimony.payment.entity.Payment;
import com.matrimony.payment.repository.PaymentRepository;
import com.matrimony.subscription.dto.ActivateSubscriptionRequest;
import com.matrimony.subscription.service.SubscriptionService;
import com.matrimony.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@AutoValidate
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;
    private final WalletService walletService;
    public Payment initPayment(PaymentInitRequest paymentInitRequest) {
        Payment payment = Payment.builder()
                .userId(paymentInitRequest.getUserId())
                .planId(paymentInitRequest.getPlanId())
                .amount(paymentInitRequest.getAmount())
                .paymentGateway(paymentInitRequest.getGateway().name())
                .status(PaymentStatus.INITIATED)
                .purpose(paymentInitRequest.getPaymentPurpose().name())
                .paymentDate(null)
                .transactionId(null)
                .build();
        return paymentRepository.save(payment);
    }

    @Transactional
    public void markPaymentSuccess(PaymentResponse paymentResponse) {
        Payment payment=  getPayment(paymentResponse.getUserId(), paymentResponse.getPaymentId());
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setTransactionId(paymentResponse.getTransactionId());
        payment.setPaymentDate(LocalDateTime.now());
        log.info("Payment marked SUCCESS userId={}, paymentId={}, txn={}", paymentResponse.getUserId(), paymentResponse.getPaymentId(), paymentResponse.getTransactionId());

        // Purpose-based action
        if (PaymentPurpose.SUBSCRIPTION.name().equals(payment.getPurpose())) {
            subscriptionService.activateSubscriptionFromPayment(ActivateSubscriptionRequest.builder().userId(paymentResponse.getUserId()).planId(payment.getPlanId()).build());
        } else if (PaymentPurpose.WALLET_RECHARGE.name().equals(payment.getPurpose())) {
            walletService.creditWallet(paymentResponse.getUserId(), payment.getAmount(),"WALLET_RECHARGE",paymentResponse.getTransactionId());
        }
    }

    @Transactional
    public void markPaymentFailed(PaymentResponse paymentResponse) {
        Payment payment= getPayment(paymentResponse.getUserId(), paymentResponse.getPaymentId());
        payment.setStatus(PaymentStatus.FAILED);
        payment.setPaymentDate(LocalDateTime.now());
        log.info("Payment marked FAILED userId={}, paymentId={}", paymentResponse.getUserId(), paymentResponse.getPaymentId());
    }

    private Payment getPayment(String userId, Long paymentId) {
        Payment payment = paymentRepository.findByIdAndUserId(paymentId, userId).orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_SUCCESS);
        }
        if (payment.getStatus() == PaymentStatus.FAILED) {
            throw new CustomException(ErrorCode.PAYMENT_ALREADY_FAILED);
        }
        return payment;
    }

    /*    @Transactional
    public void markPaymentSuccess(String transactionId) {
        Payment payment = paymentRepository.findByTransactionId(transactionId).orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_NOT_FOUND));
        if (payment.getStatus() == PaymentStatus.SUCCESS) {
            return; // already processed
        }
        payment.setStatus(PaymentStatus.SUCCESS);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);
        if ("SUBSCRIPTION".equals(payment.getPurpose())) {
            subscriptionService.purchaseSubscription(payment.getUserId(), payment.getPlanId());
        }
    }*/
}


