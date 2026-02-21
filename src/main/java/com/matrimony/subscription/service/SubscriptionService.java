package com.matrimony.subscription.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.plan.entity.Plan;
import com.matrimony.plan.entity.PlanRepository;
import com.matrimony.subscription.dto.SubscriptionStatus;
import com.matrimony.subscription.entity.Subscription;
import com.matrimony.subscription.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubscriptionService {

    private final SubscriptionRepository subscriptionRepository;
    private final PlanRepository planRepository;

    public void purchaseSubscription(String userId, Long planId) {

        Plan plan = planRepository.findByIdAndActiveTrue(planId)
                .orElseThrow(() -> new CustomException(ErrorCode.PLAN_NOT_FOUND));

        Optional<Subscription> existingOpt =
                subscriptionRepository.findByUserIdAndStatus(userId, SubscriptionStatus.ACTIVE);

        LocalDateTime now = LocalDateTime.now();

        if (existingOpt.isPresent()) {

            Subscription existing = existingOpt.get();

            // stacking logic
            existing.setExpiryDate(existing.getExpiryDate().plusDays(plan.getValidityDays()));
            existing.setTotalContactLimit(existing.getTotalContactLimit() + plan.getContactViewLimit());
            existing.setRemainingContactViews(existing.getRemainingContactViews() + plan.getContactViewLimit());
            existing.setTotalMessageLimit(existing.getTotalMessageLimit() + plan.getMessageLimit());
            existing.setRemainingMessages(existing.getRemainingMessages() + plan.getMessageLimit());

            subscriptionRepository.save(existing);

        } else {

            Subscription subscription = Subscription.builder()
                    .userId(userId)
                    .planId(plan.getId())
                    .planName(plan.getName())
                    .price(plan.getPrice())
                    .startDate(now)
                    .expiryDate(now.plusDays(plan.getValidityDays()))
                    .totalContactLimit(plan.getContactViewLimit())
                    .remainingContactViews(plan.getContactViewLimit())
                    .totalMessageLimit(plan.getMessageLimit())
                    .remainingMessages(plan.getMessageLimit())
                    .status(SubscriptionStatus.ACTIVE)
                    .build();

            subscriptionRepository.save(subscription);
        }
    }

    @Scheduled(cron = "0 0 * * * *") // every hour
    public void expireSubscriptions() {

        List<Subscription> expired =
                subscriptionRepository.findByStatusAndExpiryDateBefore(
                        SubscriptionStatus.ACTIVE, LocalDateTime.now());

        for (Subscription sub : expired) {
            sub.setStatus(SubscriptionStatus.EXPIRED);
            sub.setRemainingContactViews(0);
            sub.setRemainingMessages(0);
        }

        subscriptionRepository.saveAll(expired);
    }

    public void deductContactQuota(Subscription subscription) {

        if (subscription.getRemainingContactViews() <= 0) {
            throw new CustomException(ErrorCode.CONTACT_LIMIT_EXHAUSTED);
        }

        subscription.setRemainingContactViews(
                subscription.getRemainingContactViews() - 1
        );

        subscriptionRepository.save(subscription);
    }
}
