package com.matrimony.auth.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum UserStatus {

    ACTIVE("1", "Active"),
    OTP_PENDING("2", "Inactive"),
    BLOCKED("3", "Blocked");

    private final String code;
    private final String description;


}
