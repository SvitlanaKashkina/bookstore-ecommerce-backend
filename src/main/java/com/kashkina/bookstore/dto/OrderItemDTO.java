package com.kashkina.bookstore.dto;

import com.kashkina.bookstore.entity.Cart;
import com.kashkina.bookstore.entity.Order;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemDTO {

    private Long id;

    @NotNull(message = "Book ID must not be null")
    private Long bookId;

    @NotNull(message = "Quantity must not be null")
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than 0")
    private BigDecimal price;

    @NotNull(message = "Cart ID must not be null")
    private Long cartId;

    @NotNull(message = "Order ID must not be null")
    private Long orderId;
}
