package com.ecommerce.payment.service.impl;

import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.services.CartService;
import com.ecommerce.common.dto.resquest.StockReservationRequest;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.repository.OrderRepository;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.payment.config.StripeConfig;
import com.ecommerce.payment.entity.PaymentHistory;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.repository.PaymentHistoryRepository;
import com.ecommerce.payment.service.PaymentService;
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
public class PaymentServiceImpl implements PaymentService {
    private final CartService cartService;
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final StripeConfig stripeConfig;
    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    @Transactional
    @Override
    public String checkout(Long userId) throws StripeException, AccessDeniedException {

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

        // crear cart
        cartService.clearCart(userId);

        // create stripe session with order reference
        Session session = createStripeCheckoutSession(userId,order);

        // insert payment history
        PaymentHistory paymentHistory = PaymentHistory.builder()
                .order(order)
                .sessionId(session.getId())
                .status(PaymentStatus.INITIATED)
                .build();

        paymentHistoryRepository.save(paymentHistory);

        return session.getUrl();
    }

    @Transactional
    @Override
    public void successPayment(String sessionId) throws AccessDeniedException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Optional<PaymentHistory> optional = paymentHistoryRepository.findBySessionId(sessionId);

        if(optional.isEmpty()){
            log.warn("No payment history found for this session id = {}",sessionId);
            return;
        }

        PaymentHistory paymentHistory = optional.get();

        // update order status

        Order order = paymentHistory.getOrder();

        if(!user.getId().equals(order.getUser().getId())){
            throw new AccessDeniedException("You are not accessable for this resource.");
        }
        order.setStatus(OrderStatus.PAID);
        order = orderService.save(order);

        // update payment history status
        paymentHistory.setStatus(PaymentStatus.SUCCESS);
        paymentHistoryRepository.save(paymentHistory);

        // finalize reserved stock
        order.getItems().forEach(item->inventoryService.finalizedReserveStock(item.getProduct().getId(),item.getQuantity()));

    }

    @Override
    @Transactional
    public void handleCancelPayment(String sessionId) {
        try {

            Authentication auth = SecurityContextHolder.getContext().getAuthentication();

            String username = auth.getName();

            User user = userRepository.findByUsername(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            Session session = Session.retrieve(sessionId);

            if (session.getMetadata() == null || !session.getMetadata().containsKey("orderId")) {
                throw new IllegalStateException("Invalid Stripe session metadata");
            }

            Long orderId = Long.valueOf(session.getMetadata().get("orderId"));

            Order order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + orderId));

            if (!user.getId().equals(order.getUser().getId())) {
                throw new AccessDeniedException("Not authorized for this order");
            }

            order.setStatus(OrderStatus.CANCELLED);
            orderRepository.save(order);

            PaymentHistory paymentHistory = paymentHistoryRepository.findByOrder_Id(orderId)
                    .orElseThrow(() -> new EntityNotFoundException("Payment history not found for order id: " + orderId));

            paymentHistory.setStatus(PaymentStatus.CANCELLED);
            paymentHistoryRepository.save(paymentHistory);

            // release reserved stock
            order.getItems().forEach(item ->
                    inventoryService.releaseReservedStock(
                            item.getProduct().getId(),
                            item.getQuantity()
                    )
            );

        } catch (StripeException | AccessDeniedException e) {
            throw new RuntimeException("Stripe error occurred", e);
        }
    }

    private Session createStripeCheckoutSession(Long userId, Order order) throws StripeException {

        SessionCreateParams.Builder params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(stripeConfig.getSuccessUrl())
                .setCancelUrl(stripeConfig.getCancelUrl());

        // add order items
        order.getItems().forEach(item -> params.addLineItem(createLineItem(item)));

        // add delivery charge
        params.addLineItem(createDeliveryChargeLineItem(50));

        // ✅ IMPORTANT: Add metadata
        params.putMetadata("userId", String.valueOf(userId));
        params.putMetadata("orderId", String.valueOf(order.getId()));

        return Session.create(params.build());
    }

    private SessionCreateParams.LineItem createDeliveryChargeLineItem(double deliveryCharge) {
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
