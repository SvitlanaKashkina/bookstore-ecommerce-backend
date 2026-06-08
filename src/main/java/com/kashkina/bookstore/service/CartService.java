package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.CartDTO;
import com.kashkina.bookstore.dto.CartItemDTO;
import com.kashkina.bookstore.entity.Cart;
import com.kashkina.bookstore.entity.CartItem;
import com.kashkina.bookstore.entity.User;
import com.kashkina.bookstore.exception.CartNotFoundException;
import com.kashkina.bookstore.exception.UserNotFoundException;
import com.kashkina.bookstore.repository.CartItemRepository;
import com.kashkina.bookstore.repository.UserRepository;
import com.kashkina.bookstore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {

    private static final Logger log =
            LoggerFactory.getLogger(CartService.class);

    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    // GET cart by ID (with total)
    public CartDTO getCartById(Long id) {

        log.info("Fetching cart by id={}", id);

        Cart cart = cartRepository.findById(id)
                .orElseThrow(() ->
                        new CartNotFoundException("Cart not found with id " + id));

        List<CartItem> items =
                cartItemRepository.findByCartId(cart.getId());

        BigDecimal total = calculateTotal(items);

        log.info("Cart found: id={}, items={}, total={}",
                cart.getId(),
                items.size(),
                total
        );

        return mapToDTO(cart, items, total);
    }

    // CREATE cart
    public CartDTO createCart(CartDTO dto) {

        log.info("Creating cart for userId={}", dto.getUserId());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() ->
                        new UserNotFoundException("User not found with id " + dto.getUserId()));

        Cart cart = Cart.builder()
                .user(user)
                .build();

        Cart saved = cartRepository.save(cart);

        log.info("Cart created with id={}", saved.getId());

        return mapToDTO(saved, List.of(), BigDecimal.ZERO);
    }

    // DELETE cart
    public void deleteCart(Long id) {

        log.info("Deleting cart id={}", id);

        if (!cartRepository.existsById(id)) {
            throw new CartNotFoundException("Cart not found with id " + id);
        }

        cartRepository.deleteById(id);

        log.info("Cart deleted: id={}", id);
    }

    // TOTAL
    public BigDecimal calculateTotal(List<CartItem> items) {

        return items.stream()
                .map(item -> item.getBook().getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    // MAPPER
    private CartDTO mapToDTO(Cart cart,
                             List<CartItem> items,
                             BigDecimal total) {

        List<CartItemDTO> itemDTOs = items.stream()
                .map(this::mapItem)
                .toList();

        return CartDTO.builder()
                .id(cart.getId())
                .userId(cart.getUser().getId())
                .items(itemDTOs)
                .totalPrice(total)
                .build();
    }

    private CartItemDTO mapItem(CartItem item) {

        return CartItemDTO.builder()
                .id(item.getId())
                .cartId(item.getCart().getId())
                .bookId(item.getBook().getId())
                .quantity(item.getQuantity())
                .build();
    }
}