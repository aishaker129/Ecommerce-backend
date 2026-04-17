package com.ecommerce.payment.service.impl;

import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.services.CartService;
import com.ecommerce.common.dto.resquest.StockReservationRequest;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.payment.config.StripeConfig;
import com.ecommerce.payment.entity.PaymentHistory;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.repository.PaymentHistoryRepository;
import com.ecommerce.payment.service.PaymentServiceWebhook;
import com.ecommerce.product.services.InventoryService;
import com.ecommerce.user.entity.User;
import com.ecommerce.user.repository.UserRepository;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.nio.file.AccessDeniedException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceWebhookImpl implements PaymentServiceWebhook {
    private final UserRepository userRepository;
    private final CartService cartService;
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final StripeConfig stripeConfig;
    private final PaymentHistoryRepository paymentHistoryRepository;

    @Override
    public String checkout(Long userId) throws AccessDeniedException, StripeException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Cart cart = cartService.getCartByUserId(userId);

        if(!user.getId().equals(userId)){
            throw new AccessDeniedException("You are not accessable for this resource.");
        }

        if(cart.getItems().isEmpty()){
            throw new IllegalStateException("Cart is empty.");
        }

        // stock resarvation
        List<StockReservationRequest> requests = cart.getItems().stream()
                .map(item -> StockReservationRequest.builder()
                        .productId(item.getProduct().getId())
                        .quantity(item.getQuantity())
                        .build()
                ).toList();

        inventoryService.checkAndReserveStock(requests);

        // create order
        Order order = orderService.createOrderFromCart(userId,cart);

        // clear cart
        cartService.clearCart(userId);

        // create stripe session
        Session session = createSession(order,userId);

        PaymentHistory paymentHistory = PaymentHistory.builder()
                .order(order)
                .sessionId(session.getId())
                .status(PaymentStatus.INITIATED)
                .build();

        paymentHistoryRepository.saveAndFlush(paymentHistory);

        log.info("Saved checkout sessionId: {}", session.getId());

        return session.getUrl();
    }

    @Transactional
    @Override
    public void handleSuccess(Session sessionFromWebhook) {

        try {
            // Always retrieve latest session from Stripe
            Session session = Session.retrieve(sessionFromWebhook.getId());



            Optional<PaymentHistory> optional =
                    paymentHistoryRepository.findBySessionId(session.getId());

            if (optional.isEmpty()) {
                log.error("PaymentHistory NOT FOUND for session: {}", session.getId());
                return;
            }

            PaymentHistory paymentHistory = optional.get();

            if (paymentHistory.getStatus() == PaymentStatus.CANCELLED) {
                log.warn("Ignoring success for already cancelled session: {}", session.getId());
                return;
            }

            if (paymentHistory.getStatus() == PaymentStatus.SUCCESS) return;

            Order order = paymentHistory.getOrder();

            order.setStatus(OrderStatus.PAID);
            orderService.save(order);

            paymentHistory.setStatus(PaymentStatus.SUCCESS);

            if (session.getPaymentIntent() != null) {
                paymentHistory.setPaymentIntentId(session.getPaymentIntent());
            }

            paymentHistoryRepository.save(paymentHistory);

            order.getItems().forEach(item ->
                    inventoryService.finalizedReserveStock(
                            item.getProduct().getId(),
                            item.getQuantity()
                    )
            );

            log.info("Webhook success sessionId: {}", session.getId());

        } catch (Exception e) {
            log.error("Error in handleSuccess: ", e);
        }
    }

    @Transactional
    @Override
    public void handleExpired(String sessionId) throws AccessDeniedException {

        PaymentHistory paymentHistory = paymentHistoryRepository.findBySessionId(sessionId).orElseThrow(
                ()-> new EntityNotFoundException("Payment history not found with this session '"+sessionId+"'.")
        );

        if(paymentHistory.getStatus() != PaymentStatus.INITIATED) {
            log.info("Skipping expired webhook for session: {}", sessionId);
            return;
        }
        // update order status
        Order order = paymentHistory.getOrder();

        order.setStatus(OrderStatus.CANCELLED);
        order = orderService.save(order);

        // update payment history status
        paymentHistory.setStatus(PaymentStatus.CANCELLED);
        paymentHistoryRepository.save(paymentHistory);

        // relesed reserved stock
        order.getItems().forEach(item->inventoryService.releaseReservedStock(item.getProduct().getId(),item.getQuantity()));

    }

    @Transactional
    @Override
    public void handleRefund(String paymentIntentId) throws AccessDeniedException {

        PaymentHistory paymentHistory = paymentHistoryRepository.findByPaymentIntentId(paymentIntentId).orElseThrow(
                ()-> new EntityNotFoundException("Payment history not found with this session '"+paymentIntentId+"'.")
        );

        if(paymentHistory.getStatus().equals(PaymentStatus.REFUNDED)) return;

        // update order status
        Order order = paymentHistory.getOrder();


        order.setStatus(OrderStatus.REFUNDED);
        orderService.save(order);

        paymentHistory.setStatus(PaymentStatus.REFUNDED);
        paymentHistoryRepository.save(paymentHistory);

        // relesed reserved stock
        order.getItems().forEach(item->inventoryService.increaseStock(item.getProduct().getId(),item.getQuantity()));
    }

    private Session createSession(Order order, Long userId) throws StripeException {

        long expireAt = (System.currentTimeMillis() / 1000) + (30 * 60); // 30 minutes
        SessionCreateParams.Builder params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(stripeConfig.getSuccessUrl())
                .setCancelUrl(stripeConfig.getCancelUrl())
                .setExpiresAt(expireAt);


        // add order items
        order.getItems().forEach(item -> params.addLineItem(createLineItem(item)));

        // add delivery charge
        params.addLineItem(createDeliveryChargeLineItem(50));

        // ✅ IMPORTANT: Add metadata
        params.putMetadata("userId", String.valueOf(userId));
        params.putMetadata("orderId", String.valueOf(order.getId()));

        return Session.create(params.build());
    }

    private SessionCreateParams.LineItem createDeliveryChargeLineItem(int deliveryCharge) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(stripeConfig.getCurrency())
                                .setUnitAmount((long) (deliveryCharge * 100))
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Delivery Charge")
                                                .build()
                                ).build()
                ).build();
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem item) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(item.getQuantity().longValue())
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(stripeConfig.getCurrency())
                                .setUnitAmount((long) (item.getUnitPrice() * 100))
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(item.getProductName())
                                                .build()
                                ).build()
                ).build();
    }
}
