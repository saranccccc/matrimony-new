package com.matrimony.payment.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.payment.dto.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private Long planId; // nullable if wallet recharge

    private BigDecimal amount;

    private String transactionId; // from payment gateway

    private String paymentGateway; // RAZORPAY, STRIPE, etc.

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String purpose;
    // SUBSCRIPTION / WALLET / CONTACT_UNLOCK

    private LocalDateTime paymentDate;
}
