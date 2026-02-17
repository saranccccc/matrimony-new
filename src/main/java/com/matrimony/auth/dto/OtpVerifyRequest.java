package com.matrimony.auth.dto;

import com.matrimony.auth.entity.OtpChannel;
import lombok.Data;

@Data
public class OtpVerifyRequest {
    private String userId;
    private OtpChannel channel;
    private String otp;
}
