package com.matrimony.auth.dto;

import lombok.Data;

@Data
public class RegisterResponse {
    private String userID;
    private String smsOtp;
    private String emailOtp;
}
