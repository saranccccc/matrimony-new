package com.matrimony.unlock.dto;


import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class ContactUnlockInitiateResponse {

    private boolean alreadyUnlocked;

    private boolean unlockedDirectly;

    private Long paymentId;

    private BigDecimal amount;

    private String message;
}