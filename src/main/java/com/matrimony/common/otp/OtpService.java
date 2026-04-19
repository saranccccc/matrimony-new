package com.matrimony.common.otp;


import com.matrimony.common.entity.OtpChannel;

public interface OtpService {

    String generateAndSaveOtp(String userId, OtpChannel channel, String destination);

    void verifyOtp(String userId, OtpChannel channel, String enteredOtp);

    boolean isOtpVerified(String userId);

    void resendOtp(String userId, OtpChannel channel);
}
