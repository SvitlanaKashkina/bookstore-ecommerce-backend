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
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private static final Logger log =
            LoggerFactory.getLogger(CartItemService.class);

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final BookRepository bookRepository;

    // =========================
    // ADD TO CART (MAIN METHOD)
    // =========================
    public CartItemDTO addToCart(CartItemDTO dto) {

        log.info("Adding bookId={} to cartId={}, quantity={}",
                dto.getBookId(), dto.getCartId(), dto.getQuantity());

        Cart cart = cartRepository.findById(dto.getCartId())
                .orElseThrow(() -> new CartNotFoundException(
                        "Cart not found with id " + dto.getCartId()));

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() -> new BookNotFoundException(
                        "Book not found with id " + dto.getBookId()));

        CartItem item = cartItemRepository
                .findByCartIdAndBookId(cart.getId(), book.getId())
                .orElse(null);

        if (item != null) {

            // UPDATE EXISTING ITEM
            item.setQuantity(item.getQuantity() + dto.getQuantity());

            // refresh snapshot (important for price/title changes)
            item.setPriceAtAddition(book.getPrice());
            item.setBookTitleSnapshot(book.getTitle());

            log.info("Updated existing CartItem id={}", item.getId());

        } else {

            // CREATE NEW ITEM
            item = CartItem.builder()
                    .cart(cart)
                    .book(book)
                    .quantity(dto.getQuantity())
                    .priceAtAddition(book.getPrice())
                    .bookTitleSnapshot(book.getTitle())
                    .build();

            log.info("Created new CartItem for bookId={} in cartId={}",
                    book.getId(), cart.getId());
        }

        CartItem saved = cartItemRepository.save(item);

        log.info("CartItem saved successfully id={}", saved.getId());

        return mapToDTO(saved);
    }

    // =========================
    // GET ITEMS BY CART
    // =========================
    public List<CartItemDTO> getItemsByCart(Long cartId) {

        log.info("Fetching cart items for cartId={}", cartId);

        return cartItemRepository.findByCartId(cartId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    // =========================
    // DELETE ITEM
    // =========================
    public void delete(Long id) {

        log.info("Deleting cartItem id={}", id);

        if (!cartItemRepository.existsById(id)) {
            throw new CartItemNotFoundException(
                    "CartItem not found with id " + id);
        }

        cartItemRepository.deleteById(id);

        log.info("CartItem deleted id={}", id);
    }

    // =========================
    // UPDATE QUANTITY
    // =========================
    public CartItemDTO updateQuantity(Long id, Integer quantity) {

        log.info("Updating quantity cartItemId={} -> {}", id, quantity);

        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() ->
                        new CartItemNotFoundException(
                                "CartItem not found with id " + id));

        item.setQuantity(quantity);

        CartItem saved = cartItemRepository.save(item);

        return mapToDTO(saved);
    }

    // =========================
    // MAPPER
    // =========================
    private CartItemDTO mapToDTO(CartItem item) {

        return CartItemDTO.builder()
                .id(item.getId())
                .cartId(item.getCart().getId())
                .bookId(item.getBook().getId())
                .quantity(item.getQuantity())
                .priceAtAddition(item.getPriceAtAddition())
                .bookTitleSnapshot(item.getBookTitleSnapshot())
                .build();
    }
}
