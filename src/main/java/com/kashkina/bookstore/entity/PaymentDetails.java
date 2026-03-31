package com.kashkina.bookstore.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(
        name = "payment_details",
        indexes = {
                @Index(name = "idx_payment_details_payment", columnList = "payment_id"),
                @Index(name = "idx_payment_details_provider", columnList = "provider")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = false)
    @NotNull(message = "Payment must not be null")
    private Payment payment;

    @Column(name = "provider", nullable = false, length = 50)
    @NotBlank(message = "Provider must not be blank")
    private String provider; // PayPal, Stripe

    @Column(name = "transaction_id", nullable = false, length = 100)
    @NotBlank(message = "TransactionId must not be blank")
    private String transactionId;

    @Column(name = "payer_email", nullable = false, length = 100)
    @Email(message = "PayerEmail must be a valid email")
    @NotBlank(message = "PayerEmail must not be blank")
    private String payerEmail;

    @PrePersist
    public void prePersist() {
        // Например, можно обрезать пробелы у строк
        if (provider != null) provider = provider.trim();
        if (transactionId != null) transactionId = transactionId.trim();
        if (payerEmail != null) payerEmail = payerEmail.trim();
    }
}
