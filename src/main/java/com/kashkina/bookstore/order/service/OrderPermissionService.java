package com.kashkina.bookstore.service;

import com.kashkina.bookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderPermissionService {

    private final OrderRepository orderRepository;

    public boolean isOwner(Long orderId, Long userId) {

        return orderRepository.findById(orderId)
                .map(order -> order.getUser().getId().equals(userId))
                .orElse(false);
    }
}

