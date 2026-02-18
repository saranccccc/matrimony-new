package com.matrimony.common.otp;


import com.matrimony.auth.entity.OtpChannel;

public interface OtpService {

    void generateAndSaveOtp(String userId, OtpChannel channel, String destination);
    void verifyOtp(String userId, OtpChannel channel, String enteredOtp);
    boolean isOtpVerified(String userId);
}
