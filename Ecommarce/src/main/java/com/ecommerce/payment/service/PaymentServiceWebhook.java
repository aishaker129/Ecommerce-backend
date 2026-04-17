package com.ecommerce.payment.service;

import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;

import java.nio.file.AccessDeniedException;

public interface PaymentServiceWebhook {
    String checkout(Long userId) throws AccessDeniedException, StripeException;

    void handleSuccess(Session session) throws AccessDeniedException;

    void handleExpired(String id) throws AccessDeniedException;

    void handleRefund(String paymentIntent) throws AccessDeniedException;
}
