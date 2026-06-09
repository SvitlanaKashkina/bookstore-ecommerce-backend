package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.OrderDTO;
import com.kashkina.bookstore.dto.OrderDetailsDTO;
import com.kashkina.bookstore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@EnableMethodSecurity
public class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    // CREATE ORDER
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDTO> createOrder(@RequestBody OrderDTO dto) {
        log.info("HTTP POST /orders");
        return ResponseEntity.ok(orderService.createOrder(dto));
    }

    // GET ALL ORDERS (summary list)
    @GetMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        log.info("HTTP GET /orders");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    // GET ORDER DETAILS (FULL DTO)
    @GetMapping("/{id}")
    @PreAuthorize(
            "@orderPermissionService.isOwner(#id, authentication.principal.id) " +
                    "or hasRole('ADMIN')"
    )
    public ResponseEntity<OrderDetailsDTO> getOrderById(@PathVariable Long id) {
        log.info("HTTP GET /orders/{}", id);
        return ResponseEntity.ok(orderService.getOrderDetails(id));
    }

    // CANCEL ORDER
    @PutMapping("/{id}/cancel")
    @PreAuthorize(
            "@orderPermissionService.isOwner(#id, authentication.principal.id) " +
                    "or hasRole('ADMIN')"
    )
    public ResponseEntity<Void> cancelOrder(@PathVariable Long id) {
        log.info("Request to cancel order with id={}", id);
        orderService.cancelOrder(id);
        return ResponseEntity.noContent().build();
    }
}
