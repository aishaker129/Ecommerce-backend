package com.ecommerce.payment.service;

import com.stripe.exception.StripeException;

public interface PaymentService {
    String checkout(Long userId) throws StripeException;

    void handleSuccessfullPayment(String sessionId);
}
