package com.kashkina.bookstore.dto;

import com.kashkina.bookstore.enums.PaymentMethod;
import com.kashkina.bookstore.enums.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {

    private Long id;

    @NotNull(message = "OrderId must not be null")
    private Long orderId;

    @NotNull(message = "Amount must not be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than 0")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid number with up to 2 decimal places")
    private Double amount;

    @NotNull(message = "Status must not be null")
    private PaymentStatus status;

    @NotNull(message = "Method must not be null")
    private PaymentMethod method;

    private LocalDateTime createdAt;
}
