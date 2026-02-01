package com.matrimony.auth.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verification")
@Data
public class OtpVerification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long otpId;

    private String userId;
    private String channel;
    private String otpCode;
    private LocalDateTime expiryTime;
    private boolean verified;


}
