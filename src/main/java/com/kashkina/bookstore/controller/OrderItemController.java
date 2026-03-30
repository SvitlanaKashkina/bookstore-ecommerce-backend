package com.kashkina.bookstore.controller;

import com.kashkina.bookstore.dto.OrderItemDTO;
import com.kashkina.bookstore.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
public class OrderItemController {

    private static final Logger log = LoggerFactory.getLogger(OrderItemController.class);

    private final OrderItemService orderItemService;

    @GetMapping("/{id}")
    public ResponseEntity<OrderItemDTO> getItem(@PathVariable Long id) {
        log.info("HTTP GET /order-items/{}", id);
        return ResponseEntity.ok(orderItemService.getById(id));
    }

    @PostMapping
    public ResponseEntity<OrderItemDTO> createItem(@Valid @RequestBody OrderItemDTO dto) {
        log.info("HTTP POST /order-items - create item for bookId={}, quantity={}", dto.getBookId(), dto.getQuantity());
        return ResponseEntity.ok(orderItemService.create(dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id) {
        log.info("HTTP DELETE /order-items/{}", id);
        orderItemService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
