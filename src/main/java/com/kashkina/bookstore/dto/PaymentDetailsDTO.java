package com.kashkina.bookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetailsDTO {

    private Long id;

    @NotNull(message = "PaymentId must not be null")
    private Long paymentId;

    @NotBlank(message = "Provider must not be blank")
    @Size(max = 50, message = "Provider length must be at most 50 characters")
    private String provider; // PayPal, Stripe

    @NotBlank(message = "TransactionId must not be blank")
    @Size(max = 100, message = "TransactionId length must be at most 100 characters")
    private String transactionId;

    @NotBlank(message = "PayerEmail must not be blank")
    @Email(message = "PayerEmail must be a valid email")
    @Size(max = 100, message = "PayerEmail length must be at most 100 characters")
    private String payerEmail;
}
