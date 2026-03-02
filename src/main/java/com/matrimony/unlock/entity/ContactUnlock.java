package com.matrimony.unlock.entity;

import com.matrimony.common.entity.BaseEntity;
import jakarta.persistence.Entity;
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
@Table(name = "contact_unlocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ContactUnlock extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String requesterUserId;

    private String targetUserId;

    private BigDecimal amountCharged;

    private String paymentSource;
    // SUBSCRIPTION / WALLET

    private Long subscriptionId; // nullable

    private Long walletTransactionId; // nullable
}
