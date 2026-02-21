package com.matrimony.payment.controller;

import com.matrimony.payment.entity.Payment;
import com.matrimony.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/subscription/{planId}")
    public ResponseEntity<Payment> createSubscriptionPayment(
            @PathVariable Long planId,
            @RequestHeader("X-USER-ID") String userId) {

        return ResponseEntity.ok(
                paymentService.createSubscriptionPayment(userId, planId)
        );
    }
}
