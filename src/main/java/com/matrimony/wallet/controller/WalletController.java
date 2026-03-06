package com.matrimony.wallet.controller;

import com.matrimony.auth.security.CustomUserDetails;
import com.matrimony.wallet.dto.WalletTopupRequest;
import com.matrimony.wallet.dto.WalletTopupResponse;
import com.matrimony.wallet.entity.Wallet;
import com.matrimony.wallet.service.WalletService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user/wallet")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping
    public ResponseEntity<Wallet> getWallet(@AuthenticationPrincipal CustomUserDetails user) {
        return ResponseEntity.ok(walletService.getOrCreateWallet(user.getUserId()));
    }

    @PostMapping("/topup")
    public ResponseEntity<WalletTopupResponse> topup(@AuthenticationPrincipal CustomUserDetails user, @RequestBody WalletTopupRequest request) {
        WalletTopupResponse resp = walletService.initiateTopup(user.getUserId(), request);
        return ResponseEntity.ok(resp);
    }
}