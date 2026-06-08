package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.OrderDetailsDTO;
import com.kashkina.bookstore.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping("/{cartId}")
    @PreAuthorize(
            "@cartSecurityService.isOwner(#cartId, authentication.principal.id)"
                    + " or hasRole('ADMIN')"
    )
    public ResponseEntity<OrderDetailsDTO> checkout(@PathVariable Long cartId) {

        return ResponseEntity.ok(checkoutService.checkout(cartId));
    }
}
