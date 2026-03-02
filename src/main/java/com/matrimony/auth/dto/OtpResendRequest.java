package com.matrimony.auth.dto;

import com.matrimony.common.entity.OtpChannel;
import lombok.Data;

@Data
public class OtpResendRequest {
    private String userId;
    private OtpChannel channel;
    private String destination;
}
