package com.matrimony.wallet.dto;



import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class WalletTopupResponse {

    private Long paymentId;

    private BigDecimal amount;

    private String message;
}