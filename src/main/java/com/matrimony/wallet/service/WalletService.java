package com.matrimony.wallet.service;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.common.validator.AutoValidate;
import com.matrimony.payment.dto.PaymentInitRequest;
import com.matrimony.payment.dto.PaymentPurpose;
import com.matrimony.payment.entity.Payment;
import com.matrimony.payment.service.PaymentService;
import com.matrimony.wallet.dto.TransactionType;
import com.matrimony.wallet.dto.WalletTopupRequest;
import com.matrimony.wallet.dto.WalletTopupResponse;
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
    private final PaymentService paymentService;
    private final WalletRepository walletRepository;
    private final WalletTransactionRepository walletTransactionRepository;

    @Transactional
    public WalletTopupResponse initiateTopup(String userId, WalletTopupRequest request) {
        Payment payment = paymentService.initPayment(PaymentInitRequest.builder().userId(userId).gateway(request.getPaymentGateway()).amount(request.getAmount()).paymentPurpose(PaymentPurpose.WALLET_RECHARGE).build());
        log.info("Wallet topup initiated userId={}, paymentId={}, amount={}", userId, payment.getId(), request.getAmount());
        return WalletTopupResponse.builder()
                .paymentId(payment.getId())
                .amount(request.getAmount())
                .message("Wallet topup initiated. Complete payment.")
                .build();
    }

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
    public void creditWallet(String userId, BigDecimal amount, String referenceType, String referenceId) {
        if (amount == null || amount.signum() <= 0) {
            throw new CustomException(ErrorCode.INVALID_AMOUNT);
        }
        Wallet wallet = getOrCreateWallet(userId);
        wallet.setBalance(wallet.getBalance().add(amount));
        walletRepository.save(wallet);
        walletTransactionRepository.save(WalletTransaction.builder().userId(userId).amount(amount).type(TransactionType.CREDIT).referenceType(referenceType).referenceId(referenceId).balanceAfterTransaction(wallet.getBalance()).build());
    }

    @Transactional
    public void debitWallet(String userId, BigDecimal amount, String referenceType, String referenceId) {
        if (amount == null || amount.signum() <= 0) {
            throw new CustomException(ErrorCode.INVALID_AMOUNT);
        }
        Wallet wallet = walletRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.WALLET_NOT_FOUND));
        if (wallet.getBalance().compareTo(amount) < 0) {
            throw new CustomException(ErrorCode.INSUFFICIENT_WALLET_BALANCE);
        }
        wallet.setBalance(wallet.getBalance().subtract(amount));
        walletRepository.save(wallet);
        walletTransactionRepository.save(WalletTransaction.builder().userId(userId).amount(amount).type(TransactionType.DEBIT).referenceType(referenceType).referenceId(referenceId).balanceAfterTransaction(wallet.getBalance()).build());
    }
}

