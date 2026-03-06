package com.matrimony.subscription.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ActivateSubscriptionRequest {
    @NotBlank(message = "UserId cannot be blank")
    private String userId;
    @NotBlank(message = "PlanId cannot be blank")
    private Long planId;
}