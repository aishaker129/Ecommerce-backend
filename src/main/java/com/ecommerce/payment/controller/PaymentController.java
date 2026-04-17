package com.ecommerce.payment.controller;

import com.ecommerce.common.constants.ApiEndPoints;
import com.ecommerce.common.dto.response.ApiResponse;
import com.ecommerce.payment.dto.CheckoutResponse;
import com.ecommerce.payment.service.PaymentService;
import com.ecommerce.payment.service.PaymentServiceWebhook;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@RestController
@RequestMapping(ApiEndPoints.Payment.BASE_PAYMENT)
@RequiredArgsConstructor
@Tag(
        name = "Payment",
        description = "Payment related operation."
)
public class PaymentController {
    private final PaymentServiceWebhook paymentService;


    @PreAuthorize("isAuthenticated()")
    @PostMapping(ApiEndPoints.Payment.CHECKOUT)
    public ResponseEntity<String> checkout(@RequestParam(name = "user_id") Long userId) throws StripeException, AccessDeniedException {
        String url = paymentService.checkout(userId);
        return ResponseEntity.ok(url);
    }

//    @GetMapping(ApiEndPoints.Payment.SUCCESS)
//    public ResponseEntity<ApiResponse<Void>> paymentSuccess(@RequestParam("session_id") String sessionId) throws AccessDeniedException {
//        paymentService.successPayment(sessionId);
//        return ResponseEntity.ok(ApiResponse.success("Payment successful with session id '"+sessionId+"'."));
//    }
//
//    @GetMapping(ApiEndPoints.Payment.CANCEL)
//    public ResponseEntity<ApiResponse<String>> paymentCancel(@RequestParam("session_id") String sessionId){
//        paymentService.handleCancelPayment(sessionId);
//        return ResponseEntity.ok(ApiResponse.success("Payment cancelled."));
//    }


}
