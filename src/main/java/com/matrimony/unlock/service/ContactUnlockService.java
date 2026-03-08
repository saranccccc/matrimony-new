package com.matrimony.unlock.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.payment.dto.PaymentGateWay;
import com.matrimony.payment.dto.PaymentReferenceType;
import com.matrimony.payment.dto.PaymentStatus;
import com.matrimony.payment.entity.Payment;
import com.matrimony.payment.repository.PaymentRepository;
import com.matrimony.subscription.dto.SubscriptionStatus;
import com.matrimony.subscription.entity.Subscription;
import com.matrimony.subscription.repository.SubscriptionRepository;
import com.matrimony.unlock.dto.ContactUnlockInitiateResponse;
import com.matrimony.unlock.dto.PaymentSource;
import com.matrimony.unlock.entity.ContactUnlock;
import com.matrimony.unlock.property.PricingProperty;
import com.matrimony.unlock.repository.ContactUnlockRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContactUnlockService {

    private final ContactUnlockRepository contactUnlockRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final PricingProperty pricingProperty;
    private final PaymentRepository paymentRepository;
    @Transactional
    public ContactUnlockInitiateResponse unlockContact(String requesterUserId, String targetUserId) {
        if (requesterUserId.equals(targetUserId)) {
            throw new CustomException(ErrorCode.INVALID_OPERATION);
        }
        // Check already unlocked
        if (contactUnlockRepository.existsByRequesterUserIdAndTargetUserId(requesterUserId, targetUserId)) {
            log.info("Contact already unlocked requesterUserId={}, targetUserId={}", requesterUserId, targetUserId);
            return ContactUnlockInitiateResponse.builder().alreadyUnlocked(true).unlockedDirectly(true).message("Contact already unlocked").build();
        }

        Subscription activeSubscription = subscriptionRepository.findTopByUserIdAndStatusOrderByExpiryDateDesc(requesterUserId, SubscriptionStatus.ACTIVE).orElse(null);

        if (activeSubscription != null && activeSubscription.getExpiryDate().isBefore(LocalDateTime.now())) {
            activeSubscription.setStatus(SubscriptionStatus.EXPIRED);
            activeSubscription = null;
        }

        if (activeSubscription != null && activeSubscription.getRemainingContactViews() > 0) {
            activeSubscription.setRemainingContactViews(activeSubscription.getRemainingContactViews() - 1);
            ContactUnlock unlock = ContactUnlock.builder().requesterUserId(requesterUserId).targetUserId(targetUserId).subscriptionId(activeSubscription.getId()).paymentSource(PaymentSource.SUBSCRIPTION).build();
            contactUnlockRepository.save(unlock);
            log.info("Contact unlocked by subscription requesterUserId={}, targetUserId={}, subscriptionId={}", requesterUserId, targetUserId, activeSubscription.getId());
            return ContactUnlockInitiateResponse.builder().alreadyUnlocked(false).unlockedDirectly(true).message("Contact unlocked using subscription quota").build();
        }

        BigDecimal amount = activeSubscription != null ? pricingProperty.getSubscriberContactPrice() : pricingProperty.getNonSubscriberContactPrice();

       Payment payment = Payment.builder().userId(requesterUserId).planId(null).amount(amount).paymentGateway(PaymentGateWay.MOCK).status(PaymentStatus.INITIATED).paymentReferenceType(PaymentReferenceType.CONTACT_UNLOCK).transactionId(targetUserId) // temporary store targetUserId reference
                .paymentDate(null).build();

        paymentRepository.save(payment);


        log.info("Contact unlock payment initiated requesterUserId={}, targetUserId={}, paymentId={}, amount={}", requesterUserId, targetUserId, payment.getId(), amount);

        return ContactUnlockInitiateResponse.builder().alreadyUnlocked(false).unlockedDirectly(false).paymentId(payment.getId()).amount(amount).message("Payment required to unlock contact").build();


    }

    @Transactional
    public void completeContactUnlockPayment(Payment payment) {
        String requesterUserId = payment.getUserId();
        String targetUserId = payment.getTransactionId(); // temporary reference usage
        if (targetUserId == null || targetUserId.isBlank()) {
            throw new CustomException(ErrorCode.INVALID_REQUEST);
        }
        if (contactUnlockRepository.existsByRequesterUserIdAndTargetUserId(requesterUserId, targetUserId)) {
            log.info("Contact already unlocked after payment requesterUserId={}, targetUserId={}", requesterUserId, targetUserId);
            return;
        }
        ContactUnlock unlock = ContactUnlock.builder().requesterUserId(requesterUserId).targetUserId(targetUserId).paymentId(payment.getId()).paymentSource(PaymentSource.PAYMENT).build();
        contactUnlockRepository.save(unlock);
        log.info("Contact unlocked by payment requesterUserId={}, targetUserId={}, paymentId={}", requesterUserId, targetUserId, payment.getId());
    }
}
