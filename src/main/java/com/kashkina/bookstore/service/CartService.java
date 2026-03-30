package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.CartDTO;
import com.kashkina.bookstore.entity.Cart;
import com.kashkina.bookstore.exception.CartNotFoundException;
import com.kashkina.bookstore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final Logger log = LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;

    // Get cart by ID
    public CartDTO getCartById(Long id) {
        log.info("Fetching cart by id={}", id);

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() -> new CartNotFoundException("Cart not found with id " + id));

        log.info("Cart found: id={}", cart.getId());

        return mapToDTO(cart);
    }

    // Create cart
    public CartDTO createCart(CartDTO dto) {
        log.info("Creating cart for userId={}", dto.getUserId());

        Cart cart = Cart.builder()
                .userId(dto.getUserId())
                .build();

        Cart saved = cartRepository.save(cart);

        log.info("Cart created with id={}", saved.getId());

        return mapToDTO(saved);
    }

    // Delete cart
    public void deleteCart(Long id) {
        log.info("Deleting cart id={}", id);

        if (!cartRepository.existsById(id)) {
            log.warn("Cart not found: id={}", id);
            throw new CartNotFoundException("Cart not found with id " + id);
        }

        cartRepository.deleteById(id);

        log.info("Cart deleted: id={}", id);
    }

    private CartDTO mapToDTO(Cart cart) {
        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUserId())
                .build();
    }
}
