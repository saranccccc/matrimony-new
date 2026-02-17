package com.matrimony.auth.service;

import com.matrimony.audit.service.AuditService;

import com.matrimony.auth.entity.*;
import com.matrimony.auth.repository.OtpRepository;
import com.matrimony.auth.repository.UserRepository;
import com.matrimony.common.Event;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public String register(String name, String mobile, String email, String password) {
        log.info("User registration starts for Name:{}, mobile:{}, email:{}", name, mobile, email);
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setFullName(name);
        user.setMobileNo(mobile);
        user.setEmail(email);
        user.setUserStatus(UserStatus.OTP_PENDING);
        user.setPasswordHash(passwordEncoder.encode(password));
        auditService.log("USER", user.getUserId(), Event.REGISTER.name(), UserStatus.OTP_PENDING.toString());
        userRepository.save(user);
// todo : if uqinue index issue comes need to throw user already exists error
        auditService.log("USER", user.getUserId(),
                "REGISTER", "SUCCESS");
      //sendOtp(user.getUserId(), "SMS");
        log.info("User registration completed for Name:{}, mobile:{}, email:{} with userId:{}", name, mobile, email, user.getUserId());
        return user.getUserId();
    }


    public void activateUserIfEligible(String userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        boolean smsVerified = otpRepository
                .findTopByUserIdAndChannelOrderByIdDesc(userId, OtpChannel.SMS)
                .map(o -> o.getStatus() == OtpStatus.VERIFIED)
                .orElse(false);

        boolean emailVerified = true;

        if (user.getEmail() != null) {
            emailVerified = otpRepository
                    .findTopByUserIdAndChannelOrderByIdDesc(userId, OtpChannel.EMAIL)
                    .map(o -> o.getStatus() == OtpStatus.VERIFIED)
                    .orElse(false);
        }

        if (smsVerified && emailVerified) {
            user.setUserStatus(UserStatus.ACTIVE);
            userRepository.save(user);
            log.info("User {} activated successfully", userId);
        } else {
            log.info("User {} not eligible for activation yet", userId);
        }
    }

}
