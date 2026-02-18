package com.matrimony.common.otp;


import com.matrimony.auth.entity.OtpChannel;
import com.matrimony.common.entity.OtpEntity;
import com.matrimony.auth.entity.OtpStatus;
import com.matrimony.common.repository.OtpRepository;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class OtpServiceImpl implements OtpService {

    private final OtpGenerator otpGenerator;
    private final OtpRepository otpRepository;
    private final OtpProperties otpProperties;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void generateAndSaveOtp(String userId, OtpChannel OtpChannel, String destination) {
        // 1️⃣ Expire previous ACTIVE OTP
        otpRepository.expirePreviousActiveOtps(userId, OtpChannel);

        // 2️⃣ Generate raw OTP
        String rawOtp = otpGenerator.generateNumericOtp(otpProperties.getLength());
        String hashedOtp = passwordEncoder.encode(rawOtp);

        OtpEntity entity = OtpEntity.builder()
                .userId(userId)
                .otp(hashedOtp)
                .channel(OtpChannel)
                .status(OtpStatus.ACTIVE)
                .destination(destination)
                .attemptCount(0)
                .expiresAt(LocalDateTime.now().plusMinutes(otpProperties.getExpiryMinutes()))
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(entity);

        // TODO: Integrate SMS provider here
        log.info("Generated SMS OTP: {} for userId:{}", rawOtp,userId);
    }

    @Transactional(noRollbackFor = CustomException.class)
    public void verifyOtp(String userId, OtpChannel channel, String enteredOtp) {

        OtpEntity otp = otpRepository.findTopByUserIdAndChannelAndStatusOrderByCreatedAtDesc(userId, channel, OtpStatus.ACTIVE)
                    .orElseThrow(() -> new CustomException(ErrorCode.OTP_NOT_FOUND));

        // 1️⃣ Expiry check
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otp.setStatus(OtpStatus.EXPIRED);
            otpRepository.save(otp);
            throw new CustomException(ErrorCode.OTP_EXPIRED);
        }

        // 2️⃣ Attempt limit check
        if (otp.getAttemptCount() >= otpProperties.getMaxAttempts()) {
            otp.setStatus(OtpStatus.BLOCKED);
            otpRepository.save(otp);
            throw new CustomException(ErrorCode.OTP_ATTEMPTS_EXCEEDED);
        }

        int newAttempt = otp.getAttemptCount() + 1;

        if (newAttempt >= otpProperties.getMaxAttempts()) {
            otp.setAttemptCount(newAttempt);
            otp.setStatus(OtpStatus.BLOCKED);
            otpRepository.save(otp);
            throw new CustomException(ErrorCode.OTP_ATTEMPTS_EXCEEDED);
        }

        // 3️⃣ Match hashed OTP
        if (!passwordEncoder.matches(enteredOtp, otp.getOtp())) {
            otp.setAttemptCount(newAttempt);
            otpRepository.save(otp);
            throw new CustomException(ErrorCode.INVALID_OTP);
        }

        // 4️⃣ Success
        otp.setStatus(OtpStatus.USED);
        otpRepository.save(otp);
    }


}
