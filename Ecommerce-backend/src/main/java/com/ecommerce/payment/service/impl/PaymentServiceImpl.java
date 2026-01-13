package com.ecommerce.payment.service.impl;

import com.ecommerce.cart.entity.Cart;
import com.ecommerce.cart.repository.CartRepository;
import com.ecommerce.cart.services.CartService;
import com.ecommerce.common.dto.request.StockReservationRequest;
import com.ecommerce.order.entity.Order;
import com.ecommerce.order.entity.OrderItem;
import com.ecommerce.order.enums.OrderStatus;
import com.ecommerce.order.service.OrderService;
import com.ecommerce.payment.config.StripeConfig;
import com.ecommerce.payment.entity.PaymentHistory;
import com.ecommerce.payment.enums.PaymentStatus;
import com.ecommerce.payment.repository.PaymentHistoryRepository;
import com.ecommerce.payment.service.PaymentService;
import com.ecommerce.product.services.InventoryService;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {
    private final PaymentHistoryRepository paymentHistoryRepository;
    private final OrderService orderService;
    private final CartService cartService;
    private final InventoryService inventoryService;
    private final CartRepository cartRepository;
    private final StripeConfig stripeConfig;

    @Transactional
    @Override
    public String checkout(Long userId) throws StripeException {
        Cart cart = cartRepository.findByUserId(userId).orElseThrow(
                ()-> new IllegalStateException("Cart is empty")
        );

        // step:1 reserved stock
        List<StockReservationRequest> requests = cart.getItems().stream()
                .map(item -> StockReservationRequest.builder()
                        .productId(item.getProduct().getId())
                        .quantity(item.getQuantity())
                        .build())
                .toList();

        inventoryService.checkAndReserveStock(requests);

        // step:2 create order with new status
        Order order = orderService.createOrderFromCart(userId,cart);

        // clear cart
        cartService.clearCart(userId);

        // create stripe session with order referance
        Session seeeion = createStripecheckoutSession(userId,order);

        // insert payment history
        PaymentHistory paymentHistory = PaymentHistory.builder()
                .order(order)
                .sessionId(seeeion.getId())
                .paymentLink(seeeion.getUrl())
                .status(PaymentStatus.INITIATED)
                .build();

        paymentHistoryRepository.save(paymentHistory);

        return seeeion.getUrl();
    }

    @Transactional
    @Override
    public void handleSuccessfullPayment(String sessionId) {
        Optional<PaymentHistory> optionalPaymentHistory = paymentHistoryRepository.findBySessionId(sessionId);

        if(optionalPaymentHistory.isEmpty()){
            log.warn("No payment history found with this session: "+sessionId);
            return;
        }

        PaymentHistory paymentHistory = optionalPaymentHistory.get();

        // step:1 update order status
        Order order = paymentHistory.getOrder();
        order.setStatus(OrderStatus.PAID);
        order = orderService.save(order);

        // step:2 update payment history status
        paymentHistory.setStatus(PaymentStatus.SUCCESS);
        paymentHistoryRepository.save(paymentHistory);

        // step:3 finalized reserved stock;
        order.getOrderItems().forEach(
                orderItem -> inventoryService.finalizeReservedStock(orderItem.getProductId(),orderItem.getQuantity())
        );
    }

    private Session createStripecheckoutSession(Long userId, Order order) throws StripeException {
        SessionCreateParams.Builder paramBuilder = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(stripeConfig.getSuccessUrl())
                .setCancelUrl(stripeConfig.getCancelUrl());

        order.getOrderItems().forEach(orderItem -> paramBuilder.addLineItem(createLineItem(orderItem)));

        paramBuilder.addLineItem(createDeliveryChargeLineItem(500));
        paramBuilder.putMetadata("userId",String.valueOf(userId));

        return Session.create(paramBuilder.build());
    }

    private SessionCreateParams.LineItem createLineItem(OrderItem orderItem) {
        return SessionCreateParams.LineItem.builder()
                .setQuantity(orderItem.getQuantity().longValue())
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency(stripeConfig.getCurrency())
                                .setUnitAmount((long) (orderItem.getUnitPrice() * 100))
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName(orderItem.getProductName())
                                                .build()
                                )
                                .build()
                )
                .build();
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
                                )
                                .build()
                )
                .build();
    }
}
