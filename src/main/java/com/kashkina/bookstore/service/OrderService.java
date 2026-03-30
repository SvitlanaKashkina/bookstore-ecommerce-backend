package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.OrderDTO;
import com.kashkina.bookstore.entity.Order;
import com.kashkina.bookstore.enums.OrderStatus;
import com.kashkina.bookstore.exception.OrderNotFoundException;
import com.kashkina.bookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private final OrderRepository orderRepository;

    //Get order by id
    public OrderDTO getOrderById(Long id) {
        log.info("Fetching order by id={}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + id));

        log.info("Order found: id={}", order.getId());

        return mapToDTO(order);
    }

    // Create order
    public OrderDTO createOrder(OrderDTO dto) {
        log.info("Creating order for userId={}", dto.getUserId());

        Order order = Order.builder()
                .userId(dto.getUserId())
                .status(OrderStatus.NEW)
                .totalPrice(dto.getTotalPrice())
                .build();

        Order saved = orderRepository.save(order);

        log.info("Order created with id={}", saved.getId());

        return mapToDTO(saved);
    }

    // Cancel order
    public void cancelOrder(Long id) {
        log.info("Cancelling order id={}", id);

        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException("Order not found with id " + id));

        if (order.getStatus() == OrderStatus.PAID) {
            log.warn("Attempt to cancel paid order id={}", id);
            throw new IllegalStateException("Cannot cancel a paid order");
        }

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        log.info("Order cancelled id={}", id);
    }


    private OrderDTO mapToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .userId(order.getUserId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt())
                .build();
    }
}
