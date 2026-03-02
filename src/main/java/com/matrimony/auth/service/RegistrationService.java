package com.matrimony.auth.service;

import com.matrimony.auth.dto.OtpResendRequest;
import com.matrimony.auth.dto.OtpVerifyRequest;
import com.matrimony.auth.dto.RegisterRequest;
import com.matrimony.auth.dto.UserDto;
import com.matrimony.common.entity.OtpChannel;
import com.matrimony.common.otp.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class RegistrationService {

    private final AuthService authService;
    private final OtpService otpService;

    @Transactional
    public void registerUser(RegisterRequest req) {
        log.info("1. Save user details");
        UserDto dto = authService.register(req.getFirstName(), req.getLastName(), req.getMobileNo(), req.getEmail(), req.getPassword());

        log.info("2. Send OTP to SMS and to email if provided");
        otpService.generateAndSaveOtp(dto.getUserId(), OtpChannel.SMS, req.getMobileNo());
        if (null != req.getEmail()) {
            otpService.generateAndSaveOtp(dto.getUserId(), OtpChannel.EMAIL, req.getEmail());
        }
        log.info("Registration done - User id:{}, Profile Id:{}", dto.getUserId(), dto.getProfileId());
    }

    public void verifyOtp(OtpVerifyRequest request) {
        log.info("1. Verify OTP");
        otpService.verifyOtp(request.getUserId(), request.getChannel(), request.getOtp());

        log.info("2. Activate user if OTP(s) verified");
        authService.activateUserIfEligible(request.getUserId());
    }

    public void resendOtp(OtpResendRequest request) {
        log.info("1. Resend OTP");
        otpService.resendOtp(request.getUserId(), request.getChannel());
    }


}
