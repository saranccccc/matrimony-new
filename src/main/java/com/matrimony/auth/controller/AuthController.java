package com.matrimony.auth.controller;

import com.matrimony.auth.dto.OtpResendRequest;
import com.matrimony.auth.dto.OtpVerifyRequest;
import com.matrimony.auth.dto.RegisterRequest;
import com.matrimony.auth.service.RegistrationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final RegistrationService registrationService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterRequest req) {
       String response =  registrationService.registerUser(req);
        return ResponseEntity.ok("OTP sent"+response);
    }

    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpVerifyRequest request) {
        registrationService.verifyOtp(request);
        return ResponseEntity.ok("OTP Verified");
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestBody OtpResendRequest request) {
        registrationService.resendOtp(request);
        return ResponseEntity.ok("OTP resent");
    }
}
