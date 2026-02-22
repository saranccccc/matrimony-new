package com.matrimony.unlock.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.subscription.dto.SubscriptionStatus;
import com.matrimony.subscription.entity.Subscription;
import com.matrimony.subscription.repository.SubscriptionRepository;
import com.matrimony.unlock.entity.ContactUnlock;
import com.matrimony.unlock.repository.ContactUnlockRepository;
import com.matrimony.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactUnlockService {

    private final ContactUnlockRepository unlockRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final WalletService walletService;

    @Transactional
    public void unlockContact(String requesterId, String targetId) {

        if (requesterId.equals(targetId)) {
            throw new CustomException(ErrorCode.INVALID_OPERATION);
        }

        // Check already unlocked
        if (unlockRepository.findByRequesterUserIdAndTargetUserId(requesterId, targetId).isPresent()) {
            return; // already unlocked
        }

        Optional<Subscription> subscriptionOpt = subscriptionRepository.findByUserIdAndStatus(requesterId, SubscriptionStatus.ACTIVE);

        BigDecimal cost;
        String paymentSource;
        Long subscriptionId = null;
        Long walletTxId = null;

        if (subscriptionOpt.isPresent() && subscriptionOpt.get().getRemainingContactViews() > 0) {

            Subscription subscription = subscriptionOpt.get();

            subscription.setRemainingContactViews(subscription.getRemainingContactViews() - 1);

            subscriptionRepository.save(subscription);

            cost = BigDecimal.ZERO;
            paymentSource = "SUBSCRIPTION";
            subscriptionId = subscription.getId();

        } else {

            boolean hasActiveSubscription = subscriptionOpt.isPresent();

            cost = hasActiveSubscription ? new BigDecimal("100") : new BigDecimal("150");
            // todo need to get value from admin

            walletService.debitWallet(requesterId, cost, "CONTACT_UNLOCK", targetId);

            paymentSource = "WALLET";
        }

        unlockRepository.save(ContactUnlock.builder().requesterUserId(requesterId).targetUserId(targetId).amountCharged(cost).paymentSource(paymentSource).subscriptionId(subscriptionId).walletTransactionId(walletTxId).build());
    }
}
