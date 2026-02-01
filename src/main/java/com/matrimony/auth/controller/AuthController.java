package com.matrimony.auth.controller;

import com.matrimony.auth.dto.OtpRequest;
import com.matrimony.auth.service.AuthService;
import com.matrimony.auth.dto.RegisterRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        return ResponseEntity.ok(
                authService.register(
                        req.getFullName(),
                        req.getMobileNo(),
                        req.getEmail()));
    }
    @PostMapping("/verify-otp")
    public ResponseEntity<String> verifyOtp(@RequestBody OtpRequest request) {
        authService.verifyOtp(
                request.getUserId(),
                request.getChannel(),
                request.getOtp());
        return ResponseEntity.ok("OTP Verified");
    }

    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok("Service is running");
    }
}
