package com.ecommerce.payment.controller;

import com.ecommerce.common.constants.ApiEndPoints;
import com.ecommerce.common.dto.response.ApiResponse;
import com.ecommerce.payment.service.PaymentServiceWebhook;
import com.stripe.model.Charge;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiEndPoints.Webhook.BASE_WEBHOOK)
@Slf4j
public class WebhookController {
    @Value("${stripe.webhook-secret}")
    private String webhookSecret;

    private final PaymentServiceWebhook paymentServiceWebhook;

    @PostMapping(ApiEndPoints.Webhook.HANDLE_PAYMENT)
    public ResponseEntity<ApiResponse<String>> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
            log.info("Received webhook event: {}", event.getType());
        } catch (Exception e) {
            log.error("Webhook signature verification failed", e);
            return ResponseEntity.badRequest().build();
        }

        try {
            EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();

            StripeObject stripeObject;

            if (deserializer.getObject().isPresent()) {
                stripeObject = deserializer.getObject().get();
            } else {
                // fallback to unsafe deserialization
                stripeObject = deserializer.deserializeUnsafe();
                log.warn("Used unsafe deserialization for event: {}", event.getType());
            }

            switch (event.getType()) {

                case "checkout.session.completed" -> {
                    Session session = (Session) stripeObject;
                    log.info("Session received: {}", session.getId());

                    paymentServiceWebhook.handleSuccess(session);
                }

                case "checkout.session.expired" -> {
                    Session session = (Session) stripeObject;
                    paymentServiceWebhook.handleExpired(session.getId());
                }

                case "charge.refunded" -> {
                    Charge charge = (Charge) stripeObject;
                    paymentServiceWebhook.handleRefund(charge.getPaymentIntent());
                }
            }

        } catch (Exception e) {
            log.error("Webhook processing failed", e);
            return ResponseEntity.internalServerError().build();
        }

        return ResponseEntity.ok(ApiResponse.success("OK"));
    }

    @GetMapping("/dummy-success")
    public String success() {
        return "OK";
    }

    @GetMapping("/dummy-cancel")
    public String cancel() {
        return "Cancelled";
    }
}
