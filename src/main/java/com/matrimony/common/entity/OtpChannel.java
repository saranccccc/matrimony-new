package com.matrimony.common.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum OtpChannel {
    SMS("S", "Message"),
    EMAIL("E", "eMail"),
    BOTH("B", "Both");
    private final String code;
    private final String description;

}
