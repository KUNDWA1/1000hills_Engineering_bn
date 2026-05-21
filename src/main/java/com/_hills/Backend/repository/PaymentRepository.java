package com._hills.Backend.repository;

import com._hills.Backend.entity.Payment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByPaymentReference(String paymentReference);
    Optional<Payment> findByTransactionId(String transactionId);
    Page<Payment> findByStatus(String status, Pageable pageable);
    Page<Payment> findByOrderId(Long orderId, Pageable pageable);
    long countByStatus(String status);
}
