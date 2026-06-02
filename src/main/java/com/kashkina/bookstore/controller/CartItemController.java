package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.CartItemDTO;
import com.kashkina.bookstore.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart-items")
@RequiredArgsConstructor
@EnableMethodSecurity
public class CartItemController {

    private static final Logger log =
            LoggerFactory.getLogger(CartItemController.class);

    private final CartItemService cartItemService;

    @PostMapping
    public CartItemDTO add(@RequestBody CartItemDTO dto) {

        log.info(
                "POST /api/cart-items - add item: cartId={}, bookId={}, quantity={}",
                dto.getCartId(),
                dto.getBookId(),
                dto.getQuantity()
        );

        CartItemDTO result = cartItemService.addToCart(dto);

        log.info("Cart item created successfully. id={}", result.getId());

        return result;
    }

    @GetMapping("/{cartId}")
    public List<CartItemDTO> getByCart(@PathVariable Long cartId) {

        log.info(
                "GET /api/cart-items/{} - fetching cart items",
                cartId
        );

        List<CartItemDTO> result =
                cartItemService.getItemsByCart(cartId);

        log.info(
                "Found {} items in cart {}",
                result.size(),
                cartId
        );

        return result;
    }

    @PutMapping("/{id}")
    public CartItemDTO update(@PathVariable Long id,
                              @RequestParam Integer quantity) {

        log.info(
                "PUT /api/cart-items/{} - update quantity to {}",
                id,
                quantity
        );

        CartItemDTO result =
                cartItemService.updateQuantity(id, quantity);

        log.info(
                "Cart item updated successfully. id={}",
                result.getId()
        );

        return result;
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {

        log.info(
                "DELETE /api/cart-items/{} - deleting cart item",
                id
        );

        cartItemService.delete(id);

        log.info(
                "Cart item deleted successfully. id={}",
                id
        );
    }
}
