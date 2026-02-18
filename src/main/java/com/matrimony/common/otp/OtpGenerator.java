package com.matrimony.common.otp;


import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class OtpGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();

    public String generateNumericOtp(int length) {

        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < length; i++) {
            int digit = secureRandom.nextInt(10); // 0–9
            otp.append(digit);
        }

        return otp.toString();
    }
}
