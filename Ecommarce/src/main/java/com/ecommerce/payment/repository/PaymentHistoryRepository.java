package com.ecommerce.payment.repository;

import com.ecommerce.payment.entity.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory,Long> {
    Optional<PaymentHistory> findBySessionId(String sessionId);

    Optional<PaymentHistory> findByOrder_Id(Long orderId);

    Optional<PaymentHistory> findByPaymentIntentId(String paymentIntentId);
}
