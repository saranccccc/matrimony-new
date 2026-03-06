package com.matrimony.subscription.repository;

import com.matrimony.subscription.dto.SubscriptionStatus;
import com.matrimony.subscription.entity.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Optional<Subscription> findByUserIdAndStatus(String userId, SubscriptionStatus status);
    Optional<Subscription> findTopByUserIdAndStatusOrderByExpiryDateDesc(String userId, SubscriptionStatus status);
    List<Subscription> findByStatusAndExpiryDateBefore(SubscriptionStatus status, LocalDateTime dateTime);
}
