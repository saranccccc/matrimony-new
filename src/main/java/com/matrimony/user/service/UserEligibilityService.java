package com.matrimony.user.service;

import com.matrimony.auth.service.UserValidationService;
import com.matrimony.subscription.service.SubscriptionValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserEligibilityService {
    private final UserValidationService userValidationService;
    private final SubscriptionValidationService subscriptionValidationService;

    public boolean validateUserCanSearch(String userId){
       return userValidationService.validateUser(userId);
    }
    void validateUserCanSendInterest(String userId){
        subscriptionValidationService.validateSubscription(userId);
    }
    void validateTargetUserForInterest(String userId){

    }
}
