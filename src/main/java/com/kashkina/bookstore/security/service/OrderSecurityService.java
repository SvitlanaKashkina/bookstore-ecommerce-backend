package com.kashkina.bookstore.security.service;

import com.kashkina.bookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderSecurityService {

    private final OrderRepository orderRepository;

    public boolean isOwner(Long orderId, Long userId) {

        return orderRepository.findById(orderId)
                .map(order -> order.getUser().getId().equals(userId))
                .orElse(false);
    }
}

