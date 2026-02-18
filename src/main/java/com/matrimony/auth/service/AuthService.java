package com.matrimony.auth.service;

import com.matrimony.audit.service.AuditService;
import com.matrimony.auth.entity.User;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.auth.repository.UserRepository;
import com.matrimony.common.Event;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.common.otp.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuditService auditService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;

    public String register(String name, String mobile, String email, String password) {
        log.info("User registration starts for Name:{}, mobile:{}, email:{}", name, mobile, email);
        User user = new User();
        user.setUserId(UUID.randomUUID().toString());
        user.setFullName(name);
        user.setMobileNo(mobile);
        user.setEmail(email);
        user.setUserName(mobile);
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
        boolean pendingOtps = otpService.isOtpVerified(userId);
        if (!pendingOtps) {
            // ✅ All OTPs verified
            log.info("User {} activated successfully", userId);
            activateUser(userId);
            return;
        }
        log.info("User {} not eligible for activation yet", userId);
    }

    @Transactional
    public void activateUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
