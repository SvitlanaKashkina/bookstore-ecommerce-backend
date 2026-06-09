package com.kashkina.bookstore.service;

import com.kashkina.bookstore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartPermissionService {

    private final CartRepository cartRepository;

    public boolean isOwner(Long cartId, Long userId) {

        return cartRepository.findById(cartId)
                .map(cart -> cart.getUser().getId().equals(userId))
                .orElse(false);
    }
}

