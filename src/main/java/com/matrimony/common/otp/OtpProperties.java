package com.matrimony.common.otp;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "otp")
public class OtpProperties {

    private int length = 6;
    private long expiryMinutes = 5;
    private int maxVerificationAttempts = 3;
    private int resendInterval =60;
    private int resendLimit =3;
}
