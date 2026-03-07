package com.matrimony.wallet.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
public class CreditDebitWalletRequest {
    private String userId;
    @NotNull(message = "Amount cannot be blank")
    private BigDecimal amount;
    private String referenceType;
    private String referenceId;
}
