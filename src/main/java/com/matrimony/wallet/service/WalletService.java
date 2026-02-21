package com.matrimony.wallet.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.wallet.dto.TransactionType;
import com.matrimony.wallet.entity.Wallet;
import com.matrimony.wallet.entity.WalletTransaction;
import com.matrimony.wallet.repository.WalletRepository;
import com.matrimony.wallet.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class WalletService {

    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    @Transactional
    public void creditWallet(String userId, BigDecimal amount,
                             String referenceType, String referenceId) {

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseGet(() -> walletRepository.save(
                        Wallet.builder()
                                .userId(userId)
                                .balance(BigDecimal.ZERO)
                                .build()
                ));

        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);

        walletTransactionRepository.save(
                WalletTransaction.builder()
                        .userId(userId)
                        .amount(amount)
                        .type(TransactionType.CREDIT)
                        .referenceType(referenceType)
                        .referenceId(referenceId)
                        .balanceAfterTransaction(wallet.getBalance())
                        .build()
        );
    }

    @Transactional
    public void debitWallet(String userId, BigDecimal amount,
                            String referenceType, String referenceId) {

        Wallet wallet = walletRepository.findByUserId(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.WALLET_NOT_FOUND));

        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_WALLET_BALANCE);
        }

        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);

        walletTransactionRepository.save(
                WalletTransaction.builder()
                        .userId(userId)
                        .amount(amount)
                        .type(TransactionType.DEBIT)
                        .referenceType(referenceType)
                        .referenceId(referenceId)
                        .balanceAfterTransaction(wallet.getBalance())
                        .build()
        );
    }
}

