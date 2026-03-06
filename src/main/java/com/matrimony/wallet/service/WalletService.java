package com.matrimony.wallet.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.common.validator.AutoValidate;
import com.matrimony.wallet.dto.CreditDebitWalletRequest;
import com.matrimony.wallet.dto.TransactionType;
import com.matrimony.wallet.entity.Wallet;
import com.matrimony.wallet.entity.WalletTransaction;
import com.matrimony.wallet.repository.WalletRepository;
import com.matrimony.wallet.repository.WalletTransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
@AutoValidate
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    @Transactional
    public Wallet getOrCreateWallet(String userId) {
        return walletRepository.findByUserId(userId).orElseGet(() -> {
            Wallet wallet = Wallet.builder().userId(userId).balance(BigDecimal.ZERO).build();
            walletRepository.save(wallet);
            log.info("Wallet created for userId={}", userId);
            return wallet;
        });
    }
    @Transactional
    public void creditWallet(CreditDebitWalletRequest request) {
        if (request.getAmount() == null || request.getAmount() .signum() <= 0) {
            throw new CustomException(ErrorCode.INVALID_AMOUNT);
        }
        Wallet wallet = getOrCreateWallet(request.getUserId());
        wallet.setBalance(wallet.getBalance().add(request.getAmount()));
        walletRepository.save(wallet);
        walletTransactionRepository.save(WalletTransaction.builder().userId(request.getUserId()).amount(request.getAmount()).type(TransactionType.CREDIT).referenceType(request.getReferenceType()).referenceId(request.getReferenceId()).balanceAfterTransaction(wallet.getBalance()).build());
    }

    @Transactional
    public void debitWallet(CreditDebitWalletRequest request) {
        if (request.getAmount() == null || request.getAmount() .signum() <= 0) {
            throw new CustomException(ErrorCode.INVALID_AMOUNT);
        }
        Wallet wallet = walletRepository.findByUserId(request.getUserId()).orElseThrow(() -> new CustomException(ErrorCode.WALLET_NOT_FOUND));
        if (wallet.getBalance().compareTo(request.getAmount()) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_WALLET_BALANCE);
        }
        wallet.setBalance(wallet.getBalance().subtract(request.getAmount()));
        walletRepository.save(wallet);
        walletTransactionRepository.save(WalletTransaction.builder().userId(request.getUserId()).amount(request.getAmount()).type(TransactionType.DEBIT).referenceType(request.getReferenceType()).referenceId(request.getReferenceId()).balanceAfterTransaction(wallet.getBalance()).build());
    }
}

