package com.matrimony.payment.repository;

import com.matrimony.payment.dto.PaymentStatus;
import com.matrimony.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {

    Optional<Payment> findByTransactionId(String transactionId);

    List<Payment> findByUserId(String userId);

    Optional<Payment> findByIdAndUserId(Long id, String userId);

    long countByUserIdAndStatusAndPaymentReferenceType(String userId, PaymentStatus status, String paymentReferenceType);

}
