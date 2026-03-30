package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.OrderItemDTO;
import com.kashkina.bookstore.entity.OrderItem;
import com.kashkina.bookstore.exception.CartNotFoundException;
import com.kashkina.bookstore.exception.OrderItemNotFoundException;
import com.kashkina.bookstore.exception.OrderNotFoundException;
import com.kashkina.bookstore.repository.CartRepository;
import com.kashkina.bookstore.repository.OrderItemRepository;
import com.kashkina.bookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private static final Logger log = LoggerFactory.getLogger(OrderItemService.class);

    private final OrderItemRepository orderItemRepository;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;

    // Get item by id
    public OrderItemDTO getById(Long id) {
        log.info("Fetching order item by id={}", id);

        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() -> new OrderItemNotFoundException("OrderItem not found with id " + id));

        log.info("Order item found: id={}", item.getId());

        return mapToDTO(item);
    }

    // Create item
    public OrderItemDTO create(OrderItemDTO dto) {
        log.info("Creating order item: bookId={}, quantity={}", dto.getBookId(), dto.getQuantity());

        OrderItem item = OrderItem.builder()
                .bookId(dto.getBookId())
                .quantity(dto.getQuantity())
                .price(dto.getPrice())
                .cart(dto.getCartId() != null ? cartRepository.findById(dto.getCartId())
                        .orElseThrow(() -> new CartNotFoundException("Cart not found: id=" + dto.getCartId())) : null)
                .order(dto.getOrderId() != null ? orderRepository.findById(dto.getOrderId())
                        .orElseThrow(() -> new OrderNotFoundException("Order not found: id=" + dto.getOrderId())) : null)
                .build();

        OrderItem saved = orderItemRepository.save(item);

        log.info("Order item created with id={}", saved.getId());

        return mapToDTO(saved);
    }

    // Delete item
    public void delete(Long id) {
        log.info("Deleting order item id={}", id);

        if (!orderItemRepository.existsById(id)) {
            log.warn("Order item not found: id={}", id);
            throw new OrderItemNotFoundException("OrderItem not found with id " + id);
        }

        orderItemRepository.deleteById(id);

        log.info("Order item deleted: id={}", id);
    }

    private OrderItemDTO mapToDTO(OrderItem item) {
        return OrderItemDTO.builder()
                .id(item.getId())
                .bookId(item.getBookId())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .cartId(item.getCart() != null ? item.getCart().getId() : null)
                .orderId(item.getOrder() != null ? item.getOrder().getId() : null)
                .build();
    }
}
