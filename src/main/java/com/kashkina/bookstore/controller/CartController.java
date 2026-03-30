package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.CartDTO;
import com.kashkina.bookstore.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/carts")
public class CartController {

    private static final Logger log = LoggerFactory.getLogger(CartController.class);

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDTO> getCart(@PathVariable Long id) {
        log.info("HTTP GET /carts/{}", id);
        return ResponseEntity.ok(cartService.getCartById(id));
    }

    @PostMapping("/create")
    public ResponseEntity<CartDTO> createCart(@RequestBody CartDTO dto) {
        log.info("HTTP POST /carts");
        return ResponseEntity.ok(cartService.createCart(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCart(@PathVariable Long id) {
        log.info("HTTP DELETE /carts/{}", id);
        cartService.deleteCart(id);
        return ResponseEntity.noContent().build();
    }
}
