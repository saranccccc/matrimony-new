package com.matrimony.auth.controller;

import com.matrimony.auth.dto.OtpVerifyRequest;
import com.matrimony.auth.dto.RegisterRequest;
import com.matrimony.auth.entity.OtpStatus;
import com.matrimony.auth.service.AuthService;
import com.matrimony.auth.service.CredentialService;
import com.matrimony.auth.service.OtpService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final OtpService otpService;
    private final CredentialService credentialService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
        String userId = authService.register(req.getFullName(), req.getMobileNo(), req.getEmail(), req.getPassword());
        credentialService.setCredentials(userId,req.getMobileNo(),req.getPassword());
        otpService.generateOtp(userId, req);
        return ResponseEntity.ok("OTP sent");
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyRequest request) {
        // todo: one more logic to receive both email and sms otp and allow user if mobile no alone verified. currently we are allowing if email given, email address should be verified.
        otpService.verifyOtp(
                request.getUserId(),
                request.getChannel(),
                request.getOtp());
        authService.activateUserIfEligible(request.getUserId());
        return ResponseEntity.ok("OTP Verified");
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("Service is running");
    }
}
