package com.matrimony.wallet.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.wallet.dto.TransactionType;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "wallet_transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WalletTransaction extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private TransactionType type; // CREDIT / DEBIT

    private String referenceType;
    // PAYMENT / CONTACT_UNLOCK / REFUND

    private String referenceId; // paymentId or unlockId

    private BigDecimal balanceAfterTransaction;
}
