package com.matrimony.payment.service;

import com.matrimony.common.validator.AutoValidate;
import com.matrimony.payment.dto.PaymentInitRequest;
import com.matrimony.payment.dto.PaymentReferenceType;
import com.matrimony.payment.dto.PaymentStatus;
import com.matrimony.payment.entity.Payment;
import com.matrimony.payment.repository.PaymentRepository;
import com.matrimony.plan.entity.Plan;
import com.matrimony.plan.service.PlanService;
import com.matrimony.subscription.dto.PurchaseSubscriptionRequest;
import com.matrimony.subscription.dto.PurchaseSubscriptionResponse;
import com.matrimony.subscription.service.SubscriptionService;
import com.matrimony.wallet.dto.WalletTopupRequest;
import com.matrimony.wallet.dto.WalletTopupResponse;
import com.matrimony.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
@AutoValidate
public class PaymentInitService {

    private final PaymentRepository paymentRepository;
    private final SubscriptionService subscriptionService;
    private final WalletService walletService;
    private final PlanService planService;

    @Transactional
    public PurchaseSubscriptionResponse initiateSubscriptionPurchase(PurchaseSubscriptionRequest request) {
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

}


