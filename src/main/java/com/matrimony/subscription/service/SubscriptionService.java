package com.matrimony.subscription.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.common.validator.AutoValidate;
import com.matrimony.payment.dto.PaymentInitRequest;
import com.matrimony.payment.dto.PaymentPurpose;
import com.matrimony.payment.entity.Payment;
import com.matrimony.payment.service.PaymentService;
import com.matrimony.plan.entity.Plan;
import com.matrimony.plan.service.PlanService;
import com.matrimony.subscription.dto.ActivateSubscriptionRequest;
import com.matrimony.subscription.dto.PurchaseSubscriptionRequest;
import com.matrimony.subscription.dto.PurchaseSubscriptionResponse;
import com.matrimony.subscription.dto.SubscriptionStatus;
import com.matrimony.subscription.entity.Subscription;
import com.matrimony.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@AutoValidate
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PaymentService paymentService;
    private final PlanService planService;
    @Transactional
    public PurchaseSubscriptionResponse initiatePurchase( PurchaseSubscriptionRequest request) {
        log.info("1. Get payment gateway");
        log.info("2. Check plan details");
        Plan plan= planService.getActivePlan(request.getPlanId());
        log.info("3. Initiate the payment");
        Payment payment= paymentService.initPayment(PaymentInitRequest.builder().userId(request.getUserId()).planId(request.getPlanId()).gateway(request.getPaymentGateway()).amount(plan.getPrice()).paymentPurpose(PaymentPurpose.SUBSCRIPTION).build());
        log.info("4. Subscription payment initiated userId={}, planId={}, paymentId={}", request.getUserId(), request.getPlanId(), payment.getId());
        return PurchaseSubscriptionResponse.builder()
                .paymentId(payment.getId())
                .amount(payment.getAmount())
                .paymentGateway(request.getPaymentGateway())
                .message("Payment initiated. Call payment success API (mock) to activate subscription.")
                .build();
    }

    /**
     * Called after payment SUCCESS (webhook in future)
     */
    @Transactional
    public void activateSubscriptionFromPayment(ActivateSubscriptionRequest activateSubscriptionRequest) {
        Plan plan = planService.getActivePlan(activateSubscriptionRequest.getPlanId());
        LocalDateTime now = LocalDateTime.now();
        Subscription active = subscriptionRepository.findTopByUserIdAndStatusOrderByExpiryDateDesc(activateSubscriptionRequest.getUserId(), SubscriptionStatus.ACTIVE).orElse(null);
        if (active == null || active.getExpiryDate().isBefore(now)) {
            // create new
            Subscription sub = Subscription.builder().userId(activateSubscriptionRequest.getUserId()).planId(plan.getId()).planName(plan.getName()).price(plan.getPrice()).startDate(now).expiryDate(now.plusDays(plan.getValidityDays())).totalContactLimit(plan.getContactViewLimit()).remainingContactViews(plan.getContactViewLimit()).totalMessageLimit(plan.getMessageLimit()).remainingMessages(plan.getMessageLimit()).status(SubscriptionStatus.ACTIVE).build();
            subscriptionRepository.save(sub);
            log.info("Subscription activated (new) userId={}, subId={}", activateSubscriptionRequest.getUserId(), sub.getId());
            return;
        }

        // ✅ Stacking behavior (your choice)
        // Extend expiry from current expiry (not from now)
        LocalDateTime base = active.getExpiryDate().isAfter(now) ? active.getExpiryDate() : now;
        active.setExpiryDate(base.plusDays(plan.getValidityDays()));

        // Add quotas
        active.setTotalContactLimit(active.getTotalContactLimit() + plan.getContactViewLimit());
        active.setRemainingContactViews(active.getRemainingContactViews() + plan.getContactViewLimit());

        active.setTotalMessageLimit(active.getTotalMessageLimit() + plan.getMessageLimit());
        active.setRemainingMessages(active.getRemainingMessages() + plan.getMessageLimit());

        // snapshot update (optional) - keep latest planName/price visible
        active.setPlanId(plan.getId());
        active.setPlanName(plan.getName());
        active.setPrice(plan.getPrice());
        subscriptionRepository.save(active);
        log.info("Subscription stacked userId={}, subId={}, newExpiry={}", activateSubscriptionRequest.getUserId(), active.getId(), active.getExpiryDate());
    }

    public Subscription getActiveSubscription(String userId) {
        return subscriptionRepository.findTopByUserIdAndStatusOrderByExpiryDateDesc(userId, SubscriptionStatus.ACTIVE).orElseThrow(() -> new CustomException(ErrorCode.SUBSCRIPTION_NOT_FOUND));
    }

    private static void checkSubscriptionExpired(Subscription sub) {
        if (sub.getExpiryDate().isBefore(LocalDateTime.now())) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
            throw new CustomException(ErrorCode.SUBSCRIPTION_EXPIRED);
        }
    }

    @Scheduled(cron = "0 0 * * * *") // every hour
    public void expireSubscriptions() {
        List<Subscription> expired = subscriptionRepository.findByStatusAndExpiryDateBefore(SubscriptionStatus.ACTIVE, LocalDateTime.now());
        for (Subscription sub : expired) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
            sub.setRemainingContactViews(0);
            sub.setRemainingMessages(0);
        }
        subscriptionRepository.saveAll(expired);
    }


    @Transactional
    public void consumeContactView(String userId) {
        Subscription sub = getActiveSubscription(userId);
        checkSubscriptionExpired(sub);
        if (sub.getRemainingContactViews() <= 0) {
            throw new CustomException(ErrorCode.CONTACT_QUOTA_EXHAUSTED);
        }
        sub.setRemainingContactViews(sub.getRemainingContactViews() - 1);
        log.info("Contact view consumed userId={}, remaining={}", userId, sub.getRemainingContactViews());
        subscriptionRepository.save(sub);
    }

    @Transactional
    public void consumeMessage(String userId) {
        Subscription sub = getActiveSubscription(userId);
        checkSubscriptionExpired(sub);
        if (sub.getRemainingMessages() <= 0) {
            throw new CustomException(ErrorCode.MESSAGE_QUOTA_EXHAUSTED);
        }
        sub.setRemainingMessages(sub.getRemainingMessages() - 1);
        log.info("Message quota consumed userId={}, remaining={}", userId, sub.getRemainingMessages());
    }
    public void purchaseSubscription(String userId, Long planId) {
        Plan plan = planService.getActivePlan(planId);
        Optional<Subscription> existingSubscription = subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);
        LocalDateTime now = LocalDateTime.now();
        if (existingSubscription.isPresent() && Objects.equals(planId, existingSubscription.get().getPlanId())) {
            Subscription existing = getSubscription(existingSubscription, plan);
            subscriptionRepository.save(existing);
        } else {
            Subscription subscription = Subscription.builder().userId(userId).planId(plan.getId()).planName(plan.getName()).price(plan.getPrice()).startDate(now).expiryDate(now.plusDays(plan.getValidityDays())).totalContactLimit(plan.getContactViewLimit()).remainingContactViews(plan.getContactViewLimit()).totalMessageLimit(plan.getMessageLimit()).remainingMessages(plan.getMessageLimit()).status(SubscriptionStatus.ACTIVE).build();
            subscriptionRepository.save(subscription);
        }
    }

    private static @NonNull Subscription getSubscription(Optional<Subscription> existingSubscription, Plan plan) {
        Subscription existing = existingSubscription.get();
        // stacking logic
        existing.setExpiryDate(existing.getExpiryDate().plusDays(plan.getValidityDays()));
        existing.setTotalContactLimit(existing.getTotalContactLimit() + plan.getContactViewLimit());
        existing.setRemainingContactViews(existing.getRemainingContactViews() + plan.getContactViewLimit());
        existing.setTotalMessageLimit(existing.getTotalMessageLimit() + plan.getMessageLimit());
        existing.setRemainingMessages(existing.getRemainingMessages() + plan.getMessageLimit());
        return existing;
    }

}
