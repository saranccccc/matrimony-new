package com.matrimony.subscription.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.subscription.dto.PurchaseSubscriptionRequest;
import com.matrimony.subscription.dto.PurchaseSubscriptionResponse;
import com.matrimony.subscription.entity.Subscription;
import com.matrimony.subscription.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

    private final SubscriptionService subscriptionService;

    @PostMapping("/purchase")
    public ResponseEntity<PurchaseSubscriptionResponse> purchase(@AuthenticationPrincipal CustomUserDetails user, PurchaseSubscriptionRequest request) {
        request.setUserId(user.getUserId());
        PurchaseSubscriptionResponse resp = subscriptionService.initiatePurchase(request);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/active")
    public ResponseEntity<Subscription> active(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(subscriptionService.getActiveSubscription(user.getUserId()));
    }
}