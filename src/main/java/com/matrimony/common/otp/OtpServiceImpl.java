package com.matrimony.common.otp;


import com.matrimony.common.entity.OtpChannel;
import com.matrimony.common.entity.OtpEntity;
import com.matrimony.common.entity.OtpStatus;
import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.common.repository.OtpRepository;
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

        log.info("1. Expire previous ACTIVE OTP");
        otpRepository.expirePreviousActiveOtps(userId, OtpChannel);

        log.info("2. Generate raw OTP");
        String rawOtp = otpGenerator.generateNumericOtp(otpProperties.getLength());
        String hashedOtp = passwordEncoder.encode(rawOtp);

        log.info("3. Save OTP");
        OtpEntity entity = OtpEntity.builder()
                .userId(userId)
                .otp(hashedOtp)
                .channel(OtpChannel)
                .status(OtpStatus.ACTIVE)
                .destination(destination)
                .verificationAttemptCount(0)
                .resendCount(0)
                .expiresAt(LocalDateTime.now().plusMinutes(otpProperties.getExpiryMinutes()))
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(entity);

        log.info("4. Send OTP via Service Provider");
        // TODO: Integrate SMS provider here
        log.info("OTP Generated ----  UserId:{} Channel:{} OTP:{}", userId, OtpChannel, rawOtp);
    }

    @Transactional(noRollbackFor = CustomException.class)
    public void verifyOtp(String userId, OtpChannel channel, String enteredOtp) {

        log.info("Get the active OTP");
        OtpEntity otp = otpRepository.findTopByUserIdAndChannelAndStatusOrderByCreatedAtDesc(userId, channel, OtpStatus.ACTIVE).orElseThrow(() -> new CustomException(ErrorCode.OTP_NOT_FOUND));

        log.info("Expiry check");
        if (otp.getExpiresAt().isBefore(LocalDateTime.now())) {
            otp.setStatus(OtpStatus.EXPIRED);
            otpRepository.save(otp);
            throw new CustomException(ErrorCode.OTP_EXPIRED);
        }

        log.info("Attempt limit check");
        int newAttempt = otp.getVerificationAttemptCount() + 1;

        if (newAttempt >= otpProperties.getMaxVerificationAttempts()) {
            otp.setVerificationAttemptCount(newAttempt);
            otp.setStatus(OtpStatus.BLOCKED);
            otpRepository.save(otp);
            throw new CustomException(ErrorCode.OTP_ATTEMPTS_EXCEEDED);
        }

        log.info("Match hashed OTP");
        if (!passwordEncoder.matches(enteredOtp, otp.getOtp())) {
            otp.setVerificationAttemptCount(newAttempt);
            otpRepository.save(otp);
            throw new CustomException(ErrorCode.INVALID_OTP);
        }

        otp.setStatus(OtpStatus.USED);
        log.info("OTP verified");
        otpRepository.save(otp);
    }

    public boolean isOtpVerified(String userId) {
        return otpRepository.existsByUserIdAndStatus(userId, OtpStatus.ACTIVE);
    }

    @Override
    public void resendOtp(String userId, OtpChannel channel) {
        log.info("Select the top active OTP");
        OtpEntity latestOtp = otpRepository.findTopByUserIdAndChannelOrderByCreatedAtDesc(userId, channel).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        log.info("Check already verified");
        if (OtpStatus.USED == latestOtp.getStatus()) {
            if (channel == OtpChannel.SMS) {
                throw new CustomException(ErrorCode.SMS_OTP_ALREADY_VERIFIED);
            } else if (channel == OtpChannel.EMAIL) {
                throw new CustomException(ErrorCode.EMAIL_OTP_ALREADY_VERIFIED);
            }
        }

        log.info("Check resend limit");
        if (latestOtp.getResendCount() != null && latestOtp.getResendCount() >= otpProperties.getResendLimit()) {
            throw new CustomException(ErrorCode.MAX_RESEND_LIMIT_REACHED);
        }

        log.info("Check resend interval");
        if (latestOtp.getCreatedAt().plusSeconds(otpProperties.getResendInterval()).isAfter(LocalDateTime.now())) {
            throw new CustomException(ErrorCode.RESEND_TOO_FAST);
        }

        log.info("Set latest otp expired");
        latestOtp.setStatus(OtpStatus.EXPIRED);

        log.info("Generate new OTP");
        String rawOtp = otpGenerator.generateNumericOtp(otpProperties.getLength());
        String hashedOtp = passwordEncoder.encode(rawOtp);

        log.info("Save new OTP");
        OtpEntity newOtp = OtpEntity.builder()
                .userId(userId)
                .otp(hashedOtp)
                .channel(channel)
                .status(OtpStatus.ACTIVE)
                .destination(latestOtp.getDestination())
                .verificationAttemptCount(0)
                .resendCount(latestOtp.getResendCount() == null ? 1 : latestOtp.getResendCount() + 1)
                .expiresAt(LocalDateTime.now().plusMinutes(otpProperties.getExpiryMinutes()))
                .createdAt(LocalDateTime.now())
                .build();

        otpRepository.save(newOtp);
        log.info("Send OTP via Service Provider");
        // TODO: Integrate SMS provider here
        log.info("OTP resent ----  UserId:{} Channel:{} OTP:{}", userId, channel, rawOtp);
    }
}
