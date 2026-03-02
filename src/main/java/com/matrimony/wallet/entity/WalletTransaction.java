package com.matrimony.wallet.entity;

import com.matrimony.common.entity.BaseEntity;
import com.matrimony.wallet.dto.TransactionType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
