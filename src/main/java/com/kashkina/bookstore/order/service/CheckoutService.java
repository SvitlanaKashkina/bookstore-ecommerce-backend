package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.OrderDetailsDTO;
import com.kashkina.bookstore.dto.OrderItemDTO;
import com.kashkina.bookstore.entity.*;
import com.kashkina.bookstore.enums.OrderStatus;
import com.kashkina.bookstore.exception.CartNotFoundException;
import com.kashkina.bookstore.exception.UserNotFoundException;
import com.kashkina.bookstore.repository.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private static final Logger log =
            LoggerFactory.getLogger(CheckoutService.class);

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderRepository orderRepository;

    public OrderDetailsDTO checkout(Long cartId) {

        log.info("Starting checkout for cartId={}", cartId);

        // 1. get cart
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> {
                    log.warn("Cart not found: id={}", cartId);
                    return new CartNotFoundException("Cart not found with id " + cartId);
                });

        // 2. user (FROM RELATION)
        User user = cart.getUser();

        if (user == null) {
            throw new UserNotFoundException("User not found for cart id " + cartId);
        }

        // 3. cart items
        List<CartItem> cartItems = cartItemRepository.findByCartId(cartId);

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout empty cart");
        }

        log.info("Found {} cart items", cartItems.size());

        // 4. create order
        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.NEW)
                .build();

        // 5. order items
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> OrderItem.builder()
                        .order(order)
                        .book(item.getBook())
                        .quantity(item.getQuantity())
                        .price(item.getBook().getPrice())
                        .bookTitle(item.getBook().getTitle())
                        .build())
                .toList();

        order.setItems(orderItems);

        // 6. total
        BigDecimal total = orderItems.stream()
                .map(i -> i.getPrice()
                        .multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        order.setTotalPrice(total);

        log.info("Calculated total price={}", total);

        // 7. save order
        Order savedOrder = orderRepository.save(order);

        log.info("Order created successfully. id={}", savedOrder.getId());

        // 8. clear cart
        cartItemRepository.deleteAll(cartItems);

        log.info("Cart cleared. cartId={}", cartId);

        // 9. DTO mapping
        return OrderDetailsDTO.builder()
                .id(savedOrder.getId())
                .userId(savedOrder.getUser().getId())
                .totalPrice(savedOrder.getTotalPrice())
                .status(savedOrder.getStatus())
                .createdAt(savedOrder.getCreatedAt())
                .items(savedOrder.getItems().stream()
                        .map(i -> OrderItemDTO.builder()
                                .id(i.getId())
                                .bookId(i.getBook().getId())
                                .bookTitle(i.getBookTitle())
                                .quantity(i.getQuantity())
                                .price(i.getPrice())
                                .build())
                        .toList()
                )
                .build();
    }
}

