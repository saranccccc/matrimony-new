package com.matrimony.auth.security;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "token")
public class TokenProperties {

    private String secret = "matrimony-secret-key-very-secure-and-long-enough-256bits";
    private long expiry = 60000;

}
