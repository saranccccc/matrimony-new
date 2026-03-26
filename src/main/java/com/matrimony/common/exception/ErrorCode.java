package com.matrimony.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "User already exists"),
    USER_NOT_ACTIVE(HttpStatus.FORBIDDEN, "User not active"),
    USER_BLOCKED(HttpStatus.FORBIDDEN, "User blocked"),

    INVALID_USERNAME(HttpStatus.UNAUTHORIZED, "Invalid username"),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "Invalid password"),
    INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "Invalid credentials"),

    OTP_EXPIRED(HttpStatus.BAD_REQUEST, "OTP expired"),
    OTP_INVALID(HttpStatus.BAD_REQUEST, "Invalid OTP"),
    OTP_EXPIRED_INVALID(HttpStatus.BAD_REQUEST, "OTP expired/invalid"),
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
    MAX_RESEND_LIMIT_REACHED(HttpStatus.BAD_REQUEST, "MAX_RESEND_LIMIT_REACHED"),
    RESEND_TOO_FAST(HttpStatus.BAD_REQUEST, "RESEND_TOO_FAST"),


    PLAN_NOT_FOUND(HttpStatus.NOT_FOUND, "Plan not found"),
    PLAN_INACTIVE(HttpStatus.NOT_FOUND, "Plan in active"),
    CONTACT_QUOTA_EXHAUSTED(HttpStatus.BAD_REQUEST, "Contact limit exhausted"),
    CONTACT_ALREADY_UNLOCKED(HttpStatus.BAD_REQUEST, "Contact already unlocked"),
    CONTACT_UNLOCK_PAYMENT_REQUIRED(HttpStatus.BAD_REQUEST, "Contact unlock payment required"),
    CONTACT_UNLOCK_NOT_FOUND(HttpStatus.BAD_REQUEST, "Contact unlock not found"),
    MESSAGE_QUOTA_EXHAUSTED(HttpStatus.BAD_REQUEST, "Message limit exhausted"),

    PAYMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Payment not found"),
    PAYMENT_ALREADY_SUCCESS(HttpStatus.BAD_REQUEST, "Payment already success"),
    PAYMENT_ALREADY_FAILED(HttpStatus.BAD_REQUEST, "Payment already failed"),

    WALLET_NOT_FOUND(HttpStatus.NOT_FOUND, "Wallet not found"),
    INSUFFICIENT_WALLET_BALANCE(HttpStatus.FORBIDDEN, "Insufficient balance"),

    SUBSCRIPTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Subscription not found"),
    SUBSCRIPTION_EXPIRED(HttpStatus.NOT_FOUND, "Subscription expired"),
    INVALID_OPERATION(HttpStatus.BAD_REQUEST, "Invalid Operation"),
    INVALID_REQUEST(HttpStatus.BAD_REQUEST, "Invalid Request"),
    PROFILE_NOT_FOUND(HttpStatus.NOT_FOUND, "Profile not found"),
    PROFILE_NOT_APPROVED(HttpStatus.UNAUTHORIZED, "Profile not approved"),
    PROFILE_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "profile not ready for review"),


    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Invalid action"),
    INTEREST_NOT_FOUND(HttpStatus.NOT_FOUND, "Interest not found"),
    INTEREST_ALREADY_SENT(HttpStatus.BAD_REQUEST, "Interest already sent"),

    PHOTO_NOT_FOUND(HttpStatus.NOT_FOUND, "Photo not found"),
    PHOTO_LIMIT_REACHED(HttpStatus.BAD_REQUEST, "Photo limit reached"),
    REQUEST_ALREADY_EXISTS(HttpStatus.CONFLICT, "Request already exists"),
    REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, "Interest not found"),


    PREFERENCE_NOT_FOUND(HttpStatus.NOT_FOUND, "Preference not found"),
    CHAT_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "Chat Not allowed"),
    INVALID_PHOTO_REQUEST(HttpStatus.BAD_REQUEST, "Invalid Photo request"),
    INVALID_FILE_TYPE(HttpStatus.BAD_REQUEST, "Invalid file type"),
    S3_UPLOAD_FAILED(HttpStatus.BAD_REQUEST, "S3 upload failed"),
    PHOTO_TOO_LARGE(HttpStatus.BAD_REQUEST, "Max file size is 1MB"),
    INVALID_PHOTO_TYPE(HttpStatus.BAD_REQUEST, " Only JPG/PNG/WEBP images are allowed"),
    INVALID_OPERATION_PHOTO(HttpStatus.BAD_REQUEST, "Primary photo cant be deleted"),
    INVALID_AMOUNT(HttpStatus.BAD_REQUEST, "Invalid Amount"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong"),

    MODERATION_REQUEST_NOT_FOUND(HttpStatus.NOT_FOUND, " MODERATION not found"),
    MODERATION_REQUEST_EXISTS(HttpStatus.NOT_FOUND, " MODERATION request already exists"),
    MODERATION_ALREADY_PROCESSED(HttpStatus.BAD_REQUEST, " MODERATION already processed"),
    MODERATION_REQUEST_NOT_VALID(HttpStatus.BAD_REQUEST, "Moderation request not valid"),
    MODERATION_SELF_APPROVAL_NOT_ALLOWED(HttpStatus.BAD_REQUEST, "Not allowed");


    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    /**
     *
     *  PROFILE_NOT_FOUND("PROFILE_001", "Profile not found", HttpStatus.NOT_FOUND),
     *     PROFILE_NOT_COMPLETED("PROFILE_002", "Profile is not completed", HttpStatus.BAD_REQUEST),
     *     PROFILE_APPROVAL_ALREADY_PENDING("PROFILE_003", "Profile approval request is already pending", HttpStatus.BAD_REQUEST),
     *
     *     PHOTO_NOT_FOUND("PHOTO_001", "Photo not found", HttpStatus.NOT_FOUND),
     *     PHOTO_ALREADY_PENDING_APPROVAL("PHOTO_002", "Photo is already pending approval", HttpStatus.BAD_REQUEST),
     *     PHOTO_ALREADY_APPROVED("PHOTO_003", "Photo is already approved", HttpStatus.BAD_REQUEST),
     *     INVALID_PHOTO_STATUS_FOR_APPROVAL("PHOTO_004", "Only pending approval photos can be approved", HttpStatus.BAD_REQUEST),
     *     INVALID_PHOTO_STATUS_FOR_REJECTION("PHOTO_005", "Only pending approval photos can be rejected", HttpStatus.BAD_REQUEST),
     *     INVALID_PHOTO_STATUS_FOR_RAISE_APPROVAL("PHOTO_006", "Only uploaded or rejected photos can be raised for approval", HttpStatus.BAD_REQUEST),
     *     PHOTO_APPROVAL_ALREADY_PENDING("PHOTO_007", "Photo approval request is already pending", HttpStatus.BAD_REQUEST),
     *
     *     MODERATION_REQUEST_NOT_FOUND("MOD_001", "Moderation request not found", HttpStatus.NOT_FOUND),
     *     MODERATION_REQUEST_NOT_PENDING("MOD_002", "Moderation request is not in pending state", HttpStatus.BAD_REQUEST),
     *     INVALID_MODERATION_ACTION("MOD_003", "Invalid moderation action for this operation", HttpStatus.BAD_REQUEST);
     *
     *     private final String code;
     *     private final String message;
     *     private final HttpStatus httpStatus;
     *
     *     ErrorCode(String code, String message, HttpStatus httpStatus) {
     *         this.code = code;
     *         this.message = message;
     *         this.httpStatus = httpStatus;
     *     }
     */

}
