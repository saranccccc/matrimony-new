package com.matrimony.subscription.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SubscriptionValidationService {
  private final  SubscriptionService subscriptionService;
    public void validateSubscription(String userId){
        subscriptionService.getActiveSubscription(userId);
    }

}
