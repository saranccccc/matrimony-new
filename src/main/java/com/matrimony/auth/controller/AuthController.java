package com.matrimony.auth.controller;

import com.matrimony.auth.dto.OtpVerifyRequest;
import com.matrimony.auth.dto.RegisterRequest;
import com.matrimony.auth.entity.OtpChannel;
import com.matrimony.auth.service.AuthService;
import com.matrimony.auth.service.CredentialService;
import com.matrimony.auth.service.RegistrationService;
import com.matrimony.common.otp.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        registrationService.registerUser(req);
        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyRequest request) {
        // todo: one more logic to receive both email and sms otp and allow user if mobile no alone verified. currently we are allowing if email given, email address should be verified.
        registrationService.verifyOtp(request);
        return ResponseEntity.ok("OTP Verified");
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("Service is running");
    }
}
