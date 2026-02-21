package com.matrimony.subscription.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.subscription.dto.SubscriptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "subscriptions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Subscription extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Long planId;

    private String planName; // snapshot

    private BigDecimal price; // snapshot

    private LocalDateTime startDate;

    private LocalDateTime expiryDate;

    private Integer totalContactLimit;

    private Integer remainingContactViews;

    private Integer totalMessageLimit;

    private Integer remainingMessages;

    @Enumerated(EnumType.STRING)
    private SubscriptionStatus status;
}
