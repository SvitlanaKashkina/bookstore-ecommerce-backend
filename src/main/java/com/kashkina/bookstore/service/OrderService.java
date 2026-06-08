package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.OrderDTO;
import com.kashkina.bookstore.dto.OrderDetailsDTO;
import com.kashkina.bookstore.dto.OrderItemDTO;
import com.kashkina.bookstore.entity.Order;
import com.kashkina.bookstore.entity.OrderItem;
import com.kashkina.bookstore.entity.User;
import com.kashkina.bookstore.enums.OrderStatus;
import com.kashkina.bookstore.exception.OrderNotFoundException;
import com.kashkina.bookstore.exception.UserNotFoundException;
import com.kashkina.bookstore.repository.OrderRepository;
import com.kashkina.bookstore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    // =========================
    // GET ALL ORDERS
    // =========================
    public List<OrderDTO> getAllOrders() {

        log.info("Fetching all orders");

        return orderRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // =========================
    // GET ORDER DETAILS
    // =========================
    public OrderDetailsDTO getOrderDetails(Long id) {

        log.info("Fetching order details id={}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with id " + id));

        return mapToDetailsDTO(order);
    }

    // =========================
    // CREATE ORDER
    // =========================
    public OrderDTO createOrder(OrderDTO dto) {

        log.info("Creating order for userId={}", dto.getUserId());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found"));

        Order order = Order.builder()
                .user(user)
                .status(OrderStatus.NEW)
                .totalPrice(dto.getTotalPrice())
                .build();

        Order saved = orderRepository.save(order);

        log.info("Order created with id={}", saved.getId());

        return mapToDTO(saved);
    }

    // =========================
    // CANCEL ORDER
    // =========================
    public void cancelOrder(Long id) {

        log.info("Cancelling order id={}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found with id " + id));

        if (order.getStatus() == OrderStatus.PAID) {
            throw new IllegalStateException("Cannot cancel a paid order");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order cancelled id={}", id);
    }

    // =========================
    // MAPPERS
    // =========================

    private OrderDTO mapToDTO(Order order) {

        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .build();
    }

    private OrderDetailsDTO mapToDetailsDTO(Order order) {

        return OrderDetailsDTO.builder()
                .id(order.getId())
                .userId(order.getUser().getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus())
                .createdAt(order.getCreatedAt())
                .items(
                        order.getItems().stream()
                                .map(this::mapOrderItem)
                                .toList()
                )
                .build();
    }

    private OrderItemDTO mapOrderItem(OrderItem item) {

        return OrderItemDTO.builder()
                .id(item.getId())
                .bookId(item.getBook().getId())
                .bookTitle(item.getBook().getTitle())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }
}