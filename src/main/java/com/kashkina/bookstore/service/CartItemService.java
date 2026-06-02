package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.CartItemDTO;
import com.kashkina.bookstore.entity.Book;
import com.kashkina.bookstore.entity.Cart;
import com.kashkina.bookstore.entity.CartItem;
import com.kashkina.bookstore.exception.BookNotFoundException;
import com.kashkina.bookstore.exception.CartItemNotFoundException;
import com.kashkina.bookstore.exception.CartNotFoundException;
import com.kashkina.bookstore.repository.BookRepository;
import com.kashkina.bookstore.repository.CartItemRepository;
import com.kashkina.bookstore.repository.CartRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private static final Logger log =
            LoggerFactory.getLogger(CartItemService.class);

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    // ADD item to cart
    public CartItemDTO addToCart(CartItemDTO dto) {

        log.info("Adding book {} to cart {}",
                dto.getBookId(),
                dto.getCartId());

        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> {
                    log.warn("Cart not found: id={}", dto.getCartId());
                    return new CartNotFoundException(
                            "Cart not found with id " + dto.getCartId());
                });

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> {
                    log.warn("Book not found: id={}", dto.getBookId());
                    return new BookNotFoundException(
                            "Book not found with id " + dto.getBookId());
                });

        CartItem item = CartItem.builder()
                .cart(cart)
                .book(book)
                .quantity(dto.getQuantity())
                .build();

        CartItem saved = cartItemRepository.save(item);

        log.info("CartItem created successfully. id={}", saved.getId());

        return mapToDTO(saved);
    }

    // GET items by cart
    public List<CartItemDTO> getItemsByCart(Long cartId) {

        log.info("Fetching cart items for cart id={}", cartId);

        List<CartItemDTO> items = cartItemRepository.findByCartId(cartId)
                .stream()
                .map(this::mapToDTO)
                .toList();

        log.info("Found {} items in cart {}", items.size(), cartId);

        return items;
    }

    // DELETE item
    public void delete(Long id) {

        log.info("Deleting cart item id={}", id);

        if (!cartItemRepository.existsById(id)) {
            log.warn("Cart item not found: id={}", id);

            throw new CartItemNotFoundException(
                    "CartItem not found with id " + id);
        }

        cartItemRepository.deleteById(id);

        log.info("Cart item deleted successfully. id={}", id);
    }

    // UPDATE quantity
    public CartItemDTO updateQuantity(Long id, Integer quantity) {

        log.info(
                "Updating quantity for cart item id={} to quantity={}",
                id,
                quantity
        );

        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Cart item not found: id={}", id);
                    return new CartItemNotFoundException(
                            "CartItem not found with id " + id);
                });

        item.setQuantity(quantity);

        CartItem updated = cartItemRepository.save(item);

        log.info(
                "Cart item updated successfully. id={}, quantity={}",
                id,
                quantity
        );

        return mapToDTO(updated);
    }

    // MAPPER
    private CartItemDTO mapToDTO(CartItem item) {
        return CartItemDTO.builder()
                .id(item.getId())
                .cartId(item.getCart().getId())
                .bookId(item.getBook().getId())
                .quantity(item.getQuantity())
                .build();
    }
}
