package com.matrimony.auth.controller;

import com.matrimony.auth.dto.LoginRequest;
import com.matrimony.auth.dto.LoginResponse;
import com.matrimony.auth.dto.SetCredentialsRequest;
import com.matrimony.auth.service.CredentialService;
import com.matrimony.auth.service.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class CredentialController {

    private final CredentialService credentialService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/set-credentials")
    public ResponseEntity<String> setCredentials(@RequestBody SetCredentialsRequest request) {
        credentialService.setCredentials(request.getUserId(), request.getUsername(), request.getPassword());
        return ResponseEntity.ok("Credentials set successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(credentialService.login(request));
    }
}
