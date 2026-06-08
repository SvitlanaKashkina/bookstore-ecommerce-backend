package com.kashkina.bookstore.dto;

import com.kashkina.bookstore.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDetailsDTO {

    private Long id;

    private Long userId;

    private BigDecimal totalPrice;

    private OrderStatus status;

    private LocalDateTime createdAt;

    private List<OrderItemDTO> items;

}
