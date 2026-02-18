package com.matrimony.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verification")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OtpEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    @Enumerated(EnumType.STRING)
    private OtpChannel channel;

    private String destination;   // mobile or email
    private String otp;

    @Enumerated(EnumType.STRING)
    private OtpStatus status;

    private LocalDateTime expiresAt;

    // ✅ NEW FIELDS (allowed addition)
    private int attemptCount;
    private LocalDateTime createdAt;
}
