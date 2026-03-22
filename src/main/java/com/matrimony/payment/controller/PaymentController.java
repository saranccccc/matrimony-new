package com.matrimony.payment.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.common.dto.Result;
import com.matrimony.payment.dto.PaymentResponse;
import com.matrimony.payment.service.PaymentInitService;
import com.matrimony.payment.service.PaymentProcessingService;
import com.matrimony.subscription.dto.PurchaseSubscriptionRequest;
import com.matrimony.subscription.dto.PurchaseSubscriptionResponse;
import com.matrimony.wallet.dto.WalletTopupRequest;
import com.matrimony.wallet.dto.WalletTopupResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentProcessingService paymentProcessingService;
    private final PaymentInitService paymentInitService;


    @PostMapping("/subscription/init")
    public ResponseEntity<PurchaseSubscriptionResponse> purchaseSubscription(@AuthenticationPrincipal CustomUserDetails user,@Valid @RequestBody PurchaseSubscriptionRequest request) {
        request.setUserId(user.getUserId());
        PurchaseSubscriptionResponse resp = paymentInitService.initiateSubscriptionPurchase(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/wallet/topup")
    public ResponseEntity<WalletTopupResponse> topupWallet(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody WalletTopupRequest request) {
        request.setUserId(user.getUserId());
        WalletTopupResponse resp = paymentInitService.initiateWalletTopup(request);
        return ResponseEntity.ok(resp);
    }

    @PostMapping("/success")
    public ResponseEntity<Result> success(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody PaymentResponse paymentResponse) {
        paymentResponse.setUserId(user.getUserId());
        // todo; transid unique
        paymentProcessingService.markPaymentSuccess(paymentResponse);
        return ResponseEntity.ok(Result.builder().status(1).description("PAYMENT_SUCCESS").build());
    }

    @PostMapping("/failed")
    public ResponseEntity<Result> failed(@AuthenticationPrincipal CustomUserDetails user, @Valid @RequestBody PaymentResponse paymentResponse) {
        paymentResponse.setUserId(user.getUserId());
        paymentProcessingService.markPaymentFailed(paymentResponse);
        return ResponseEntity.ok(Result.builder().status(1).description("PAYMENT_FAILED").build());
    }

}
