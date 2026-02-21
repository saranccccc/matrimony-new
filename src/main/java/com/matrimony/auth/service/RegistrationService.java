package com.matrimony.auth.service;

import com.matrimony.auth.dto.OtpVerifyRequest;
import com.matrimony.auth.dto.RegisterRequest;
import com.matrimony.common.entity.OtpChannel;
import com.matrimony.common.otp.OtpService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RegistrationService {

    private final AuthService authService;
    private final OtpService otpService;

    @Transactional
    public void registerUser(RegisterRequest req) {
        String userId = authService.register(req.getFirstName(), req.getLastName(), req.getMobileNo(), req.getEmail(), req.getPassword());
        otpService.generateAndSaveOtp(userId, OtpChannel.SMS, req.getMobileNo());
        if (null != req.getEmail())
            otpService.generateAndSaveOtp(userId, OtpChannel.EMAIL, req.getEmail());
    }

    public void verifyOtp(OtpVerifyRequest request) {
        otpService.verifyOtp(
                request.getUserId(),
                request.getChannel(),
                request.getOtp());
        authService.activateUserIfEligible(request.getUserId());
    }


}
