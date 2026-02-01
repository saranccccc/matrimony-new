package com.matrimony.auth.dto;

import lombok.Data;

@Data
public class OtpRequest {
    private String userId;
    private String channel;
    private String otp;
}
