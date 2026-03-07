package com.matrimony.subscription.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActivateSubscriptionRequest {
    private String userId;
    @NotNull(message = "PlanId cannot be blank")
    private Long planId;
}