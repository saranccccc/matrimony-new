package com.matrimony.payment.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.common.validator.AutoValidate;
import com.matrimony.payment.dto.PaymentInitRequest;
import com.matrimony.payment.dto.PaymentReferenceType;
import com.matrimony.payment.dto.PaymentResponse;
import com.matrimony.payment.dto.PaymentStatus;
import com.matrimony.payment.entity.Payment;
import com.matrimony.payment.repository.PaymentRepository;
import com.matrimony.plan.entity.Plan;
import com.matrimony.plan.service.PlanService;
import com.matrimony.subscription.dto.ActivateSubscriptionRequest;
import com.matrimony.subscription.dto.PurchaseSubscriptionRequest;
import com.matrimony.subscription.dto.PurchaseSubscriptionResponse;
import com.matrimony.subscription.service.SubscriptionService;
import com.matrimony.wallet.dto.CreditDebitWalletRequest;
import com.matrimony.wallet.dto.WalletTopupRequest;
import com.matrimony.wallet.dto.WalletTopupResponse;
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
    private final PlanService planService;

    @Transactional
    public PurchaseSubscriptionResponse initiatePurchase(PurchaseSubscriptionRequest request) {
        log.info("1. Get payment gateway");
        log.info("2. Check plan details");
        Plan plan= planService.getActivePlan(request.getPlanId());
        log.info("3. Initiate the payment");
        Payment payment= initPayment(PaymentInitRequest.builder().userId(request.getUserId()).planId(request.getPlanId()).gateway(request.getPaymentGateway()).amount(plan.getPrice()).paymentReferenceType(PaymentReferenceType.SUBSCRIPTION).build());
        log.info("4. Subscription payment initiated userId={}, planId={}, paymentId={}", request.getUserId(), request.getPlanId(), payment.getId());
        return PurchaseSubscriptionResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .paymentGateway(request.getPaymentGateway())
                .message("Payment initiated. Call payment success API (mock) to activate subscription.")
                .build();
    }

    @Transactional
    public WalletTopupResponse initiateWalletTopup(WalletTopupRequest request) {
        Payment payment = initPayment(PaymentInitRequest.builder().userId(request.getUserId()).gateway(request.getPaymentGateway()).amount(request.getAmount()).paymentReferenceType(PaymentReferenceType.WALLET_RECHARGE).build());
        log.info("Wallet topup initiated userId={}, paymentId={}, amount={}", request.getUserId(), payment.getId(), request.getAmount());
        return WalletTopupResponse.builder()
                .paymentId(payment.getId())
                .amount(request.getAmount())
                .message("Wallet topup initiated. Complete payment.")
                .build();
    }
    public Payment initPayment(PaymentInitRequest paymentInitRequest) {
        Payment payment = Payment.builder()
                .userId(paymentInitRequest.getUserId())
                .planId(paymentInitRequest.getPlanId())
                .amount(paymentInitRequest.getAmount())
                .paymentGateway(paymentInitRequest.getGateway())
                .status(PaymentStatus.INITIATED)
                .paymentReferenceType(paymentInitRequest.getPaymentReferenceType())
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
        if (PaymentReferenceType.SUBSCRIPTION.equals(payment.getPaymentReferenceType())) {
            subscriptionService.activateSubscriptionFromPayment(ActivateSubscriptionRequest.builder().userId(paymentResponse.getUserId()).planId(payment.getPlanId()).build());
        } else if (PaymentReferenceType.WALLET_RECHARGE.equals(payment.getPaymentReferenceType())) {
             walletService.creditWallet( CreditDebitWalletRequest.builder().userId(paymentResponse.getUserId()).amount(payment.getAmount()).referenceType("WALLET_RECHARGE").referenceId(paymentResponse.getTransactionId()).build());
        } else if (PaymentReferenceType.CONTACT_UNLOCK.equals(payment.getPaymentReferenceType())) {
            walletService.creditWallet( CreditDebitWalletRequest.builder().userId(paymentResponse.getUserId()).amount(payment.getAmount()).referenceType("WALLET_RECHARGE").referenceId(paymentResponse.getTransactionId()).build());
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


