package com.ecommerce.payment.controller;

import com.ecommerce.common.constants.ApiEndpoint;
import com.ecommerce.common.dto.resonse.ApiResponse;
import com.ecommerce.payment.dto.CheckoutResponse;
import com.ecommerce.payment.service.PaymentService;
import com.stripe.exception.StripeException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(ApiEndpoint.Payment.BASE_PAYMENT)
@RequiredArgsConstructor
@Tag(
        name = "Payment",
        description = "Operations for handling payments and Stripe integration"
)
public class PaymentController {

    private  final PaymentService paymentService;

    @Operation(
            summary = "Initiate checkout process",
            description = "Creates a Stripe checkout session and returns the URL for the user to complete payment.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Checkout session created successfully",
                            content = @Content(schema = @Schema(implementation = CheckoutResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "400",
                            description = "Invalid user ID or bad request",
                            content = @Content
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "500",
                            description = "Stripe integration error",
                            content = @Content
                    )
            }
    )
    @PostMapping(ApiEndpoint.Payment.CHECKOUT)
    public ResponseEntity<ApiResponse<CheckoutResponse>> checkout(@RequestParam("user_Id") Long userId) throws StripeException {
        return ResponseEntity.ok(ApiResponse.success(
                CheckoutResponse.builder()
                        .checkoutUrl(paymentService.checkout(userId))
                        .build())
        );
    }

    @Operation(
            summary = "Handle successful payment",
            description = "Callback endpoint for Stripe to report successful payment. Updates order status and inventory.",
            responses = {
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "200",
                            description = "Payment processed successfully",
                            content = @Content(schema = @Schema(implementation = ApiResponse.class))
                    ),
                    @io.swagger.v3.oas.annotations.responses.ApiResponse(
                            responseCode = "404",
                            description = "Session ID not found",
                            content = @Content
                    )
            }
    )
    @GetMapping(ApiEndpoint.Payment.SUCCESS)
    public ResponseEntity<ApiResponse<Void>> paymentSuccess(@RequestParam("sessionId") String sessionId){
        paymentService.handleSuccessfullPayment(sessionId);
        return ResponseEntity.ok(ApiResponse.success("Payment successful! Session with id: "+sessionId));
    }
}
