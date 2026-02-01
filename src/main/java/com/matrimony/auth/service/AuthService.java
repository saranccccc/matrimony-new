package com.matrimony.auth.service;

import com.matrimony.audit.service.AuditService;
import com.matrimony.auth.entity.OtpVerification;
import com.matrimony.auth.entity.User;
import com.matrimony.auth.repository.OtpRepository;
import com.matrimony.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuditService auditService;
    private final OtpRepository otpRepository;

    public String register(String name, String mobile, String email) {
        log.info("User registration starts for Name:{}, mobile:{}, email:{}", name, mobile, email);
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setFullName(name);
        user.setMobileNo(mobile);
        user.setEmail(email);
        user.setStatus("PENDING");
        auditService.log("USER", user.getUserId(),
                "REGISTER", "PENDING");
        userRepository.save(user);

        auditService.log("USER", user.getUserId(),
                "REGISTER", "SUCCESS");
        sendOtp(user.getUserId(), "SMS");
        log.info("User registration completed for Name:{}, mobile:{}, email:{} with userId:{}", name, mobile, email, user.getUserId());
        return user.getUserId();
    }

    public void sendOtp(String userId, String channel) {
        log.info("Sending OTP for user: {} by channel:{}", userId, channel);
        OtpVerification otp = new OtpVerification();
        otp.setUserId(userId);
        otp.setChannel(channel);
        // todo : dynamic opt creation
        otp.setOtpCode("123456"); // later random
        otp.setExpiryTime(LocalDateTime.now().plusMinutes(5));
        otp.setVerified(false);
        otpRepository.save(otp);
        log.info("OTP sent for user: {} by channel:{}", userId, channel);
    }

    public void verifyOtp(String userId, String channel, String otpCode) {
        log.info("Verifying OTP for user: {} by channel:{},otpCode{}", userId, channel, otpCode);
        OtpVerification otp = otpRepository
                .findTopByUserIdAndChannelOrderByOtpIdDesc(userId, channel)
                .orElseThrow(() -> new RuntimeException("OTP not found"));

        if (otp.isVerified()) {
            log.info("OTP already verified for User:{}",userId);
            throw new RuntimeException("OTP already verified");
        }

        if (otp.getExpiryTime().isBefore(LocalDateTime.now())) {
            log.info("OTP expired for User:{}",userId);
            throw new RuntimeException("OTP expired");
        }

        if (!otp.getOtpCode().equals(otpCode)) {
            log.info("Invalid OTP for User:{}",userId);
            throw new RuntimeException("Invalid OTP");
        }

        otp.setVerified(true);
        otpRepository.save(otp);

        User user = userRepository.findById(userId).orElseThrow();
        user.setStatus("OTP_VERIFIED");
        userRepository.save(user);
        log.info("Verified OTP for user: {} by channel:{}", userId, channel);
    }
}
