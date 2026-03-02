package com.matrimony.auth.service;

import com.matrimony.audit.service.AuditService;
import com.matrimony.auth.dto.UserDto;
import com.matrimony.auth.entity.User;
import com.matrimony.auth.entity.UserStatus;
import com.matrimony.auth.mapper.UserMapper;
import com.matrimony.auth.repository.UserRepository;
import com.matrimony.common.Event;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.common.otp.OtpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final AuditService auditService;
    private final OtpService otpService;
    private final PasswordEncoder passwordEncoder;
    private final ProfileIdGenerator profileIdGenerator;
    private final UserMapper userMapper;

    public UserDto register(String firstName, String lastName, String mobile, String email, String password) {
        log.info("User registration starts for Name:{}, mobile:{}, email:{}", firstName, mobile, email);
        User user = new User();
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setMobileNo(mobile);
        user.setEmail(email);
        user.setUserStatus(UserStatus.OTP_PENDING);
        user.setProfileId(profileIdGenerator.generateProfileId());
        user.setPasswordHash(passwordEncoder.encode(password));
        auditService.log("USER", user.getFirstName(), Event.REGISTER.name(), UserStatus.OTP_PENDING.toString());
        userRepository.save(user);
        // todo : if unique index issue comes need to throw user already exists error
        auditService.log("USER", user.getUserId(), "REGISTER", "SUCCESS");
        log.info("User registration completed for Name:{}, mobile:{}, email:{} with userId:{}", firstName, mobile, email, user.getUserId());
        return userMapper.toResponse(user);
    }


    public void activateUserIfEligible(String userId) {
        log.info("User activation starts for Id:{}", userId);
        log.info("Check User exists");
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("USER_NOT_FOUND"));

        log.info("Check pending OTPS");
        boolean pendingOtps = otpService.isOtpVerified(userId);
        if (!pendingOtps) {
            log.info("All OTPs verified");
            activateUser(userId);
            log.info("User {} activated successfully", userId);
            return;
        }
        log.info("User {} not eligible for activation yet", userId);
    }


    public void activateUser(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        user.setUserStatus(UserStatus.ACTIVE);
        userRepository.save(user);
    }
}
