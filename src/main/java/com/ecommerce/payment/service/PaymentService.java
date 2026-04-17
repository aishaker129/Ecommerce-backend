package com.ecommerce.payment.service;

import com.stripe.exception.StripeException;

import java.nio.file.AccessDeniedException;

public interface PaymentService {
    String checkout(Long userId) throws StripeException, AccessDeniedException;

    void successPayment(String sessionId) throws AccessDeniedException;

    void handleCancelPayment(String sessionId);
}
