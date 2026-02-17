package com.matrimony.auth.service;

import com.matrimony.auth.dto.RegisterRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;

import com.matrimony.auth.entity.*;
import com.matrimony.auth.repository.OtpRepository;
import org.springframework.util.StringUtils;

@Slf4j
@Service
@RequiredArgsConstructor
public class OtpService {

    private final OtpRepository otpRepository;

    public void generateOtp(String userId, RegisterRequest req) {
        generateOtp(userId,OtpChannel.SMS,req.getMobileNo());
        if(StringUtils.hasText(req.getEmail())) {
            generateOtp(userId, OtpChannel.EMAIL, req.getEmail());
        }
    }

    public void generateOtp(String userId, OtpChannel channel, String destination) {
        String otp = "123456"; // demo
        // todo: service to generate otp
// todo: dynamo db otp save into otp table
        otpRepository.save(
                OtpEntity.builder()
                        .userId(userId)
                        .channel(channel)
                        .destination(destination)
                        .otp(otp)
                        .status(OtpStatus.SENT)
                        .expiresAt(LocalDateTime.now().plusMinutes(5))
                        .build()
        );

        log.info("OTP generated | userId={} | channel={} | dest={}",
                userId, channel, destination);
    }

    public void verifyOtp(String userId, OtpChannel channel, String otp) {

        OtpEntity entity = otpRepository
                .findTopByUserIdAndChannelOrderByIdDesc(userId, channel)
                .orElseThrow(() -> new RuntimeException(channel + "_OTP_NOT_FOUND"));

        if (entity.getStatus() == OtpStatus.VERIFIED)
            throw new RuntimeException(channel + "_OTP_ALREADY_VERIFIED");

        if (entity.getExpiresAt().isBefore(LocalDateTime.now()))
            throw new RuntimeException(channel + "_OTP_EXPIRED");

        if (!entity.getOtp().equals(otp))
            throw new RuntimeException(channel + "_OTP_INVALID");

        entity.setStatus(OtpStatus.VERIFIED);
        otpRepository.save(entity);

        log.info("OTP verified | userId={} | channel={}", userId, channel);


    }

}
