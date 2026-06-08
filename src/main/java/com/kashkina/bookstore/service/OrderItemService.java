package com.kashkina.bookstore.service;

import com.kashkina.bookstore.dto.OrderItemDTO;
import com.kashkina.bookstore.entity.Book;
import com.kashkina.bookstore.entity.Order;
import com.kashkina.bookstore.entity.OrderItem;
import com.kashkina.bookstore.exception.BookNotFoundException;
import com.kashkina.bookstore.exception.OrderItemNotFoundException;
import com.kashkina.bookstore.exception.OrderNotFoundException;
import com.kashkina.bookstore.repository.BookRepository;
import com.kashkina.bookstore.repository.OrderItemRepository;
import com.kashkina.bookstore.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderItemService {

    private static final Logger log =
            LoggerFactory.getLogger(OrderItemService.class);

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final BookRepository bookRepository;

    // GET by id
    public OrderItemDTO getById(Long id) {

        log.info("Fetching order item id={}", id);

        OrderItem item = orderItemRepository.findById(id)
                .orElseThrow(() ->
                        new OrderItemNotFoundException("OrderItem not found with id " + id));

        return mapToDTO(item);
    }

    // CREATE
    public OrderItemDTO create(Long orderId, OrderItemDTO dto) {

        log.info("Creating order item: orderId={}, bookId={}, quantity={}",
                orderId, dto.getBookId(), dto.getQuantity());

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new OrderNotFoundException("Order not found: id=" + orderId));

        Book book = bookRepository.findById(dto.getBookId())
                .orElseThrow(() ->
                        new BookNotFoundException("Book not found: id=" + dto.getBookId()));

        OrderItem item = OrderItem.builder()
                .order(order)
                .book(book)
                .quantity(dto.getQuantity())
                .price(book.getPrice())
                .bookTitle(book.getTitle())
                .build();

        OrderItem saved = orderItemRepository.save(item);

        log.info("Order item created id={}", saved.getId());

        return mapToDTO(saved);
    }

    // DELETE
    public void delete(Long id) {

        log.info("Deleting order item id={}", id);

        if (!orderItemRepository.existsById(id)) {
            throw new OrderItemNotFoundException("OrderItem not found with id " + id);
        }

        orderItemRepository.deleteById(id);
    }

    // MAPPER
    private OrderItemDTO mapToDTO(OrderItem item) {

        return OrderItemDTO.builder()
                .id(item.getId())
                .orderId(item.getOrder().getId())
                .bookId(item.getBook().getId())
                .bookTitle(item.getBookTitle())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }
}