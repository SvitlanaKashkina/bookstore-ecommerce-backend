package com.kashkina.bookstore.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderDTO {

    private Long id;

    @NotNull(message = "User ID must not be null")
    private Long userId;

    @NotNull(message = "Total price must not be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Total price must be non-negative")
    private BigDecimal totalPrice;

    @NotBlank(message = "Status must not be blank")
    @Size(max = 50, message = "Status max length is 50")
    private String status;

    private LocalDateTime createdAt;
}
