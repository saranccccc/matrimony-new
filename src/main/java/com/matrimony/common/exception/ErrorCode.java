package com.matrimony.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "User already exists"),
    USER_NOT_ACTIVE(HttpStatus.FORBIDDEN, "User not active"),

    INVALID_USERNAME(HttpStatus.UNAUTHORIZED, "Invalid username"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid password"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials"),

    OTP_EXPIRED(HttpStatus.BAD_REQUEST, "OTP expired"),
    OTP_INVALID(HttpStatus.BAD_REQUEST, "Invalid OTP"),
    SMS_OTP_INVALID(HttpStatus.BAD_REQUEST, "Invalid SMS OTP"),
    SMS_OTP_EXPIRED(HttpStatus.BAD_REQUEST, "SMS OTP expired"),
    SMS_OTP_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "SMS OTP already verified"),

    EMAIL_OTP_INVALID(HttpStatus.BAD_REQUEST, "Invalid Email OTP"),
    EMAIL_OTP_EXPIRED(HttpStatus.BAD_REQUEST, "Email OTP expired"),
    EMAIL_OTP_ALREADY_VERIFIED(HttpStatus.BAD_REQUEST, "Email OTP already verified"),
    OTP_NOT_FOUND(HttpStatus.NOT_FOUND, "OTP not found"),
    INVALID_OTP(HttpStatus.BAD_REQUEST, "Invalid OTP"),
    OTP_ATTEMPTS_EXCEEDED(HttpStatus.FORBIDDEN, "Maximum OTP attempts exceeded"),
    OTP_RESEND_TOO_FAST(HttpStatus.TOO_MANY_REQUESTS, "Please wait before requesting OTP again"),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid JWT token"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Access denied"),

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
