package com.kashkina.bookstore.entity;

import com.kashkina.bookstore.enums.PaymentMethod;
import com.kashkina.bookstore.enums.PaymentStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "payments",
        indexes = {
                @Index(name = "idx_payment_order", columnList = "order_id"),
                @Index(name = "idx_payment_status", columnList = "status")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "order_id", nullable = false)
    @NotNull(message = "Order must not be null")
    private Order order;

    @Column(name = "amount", nullable = false)
    @NotNull(message = "Amount must not be null")
    @Digits(integer = 10, fraction = 2, message = "Amount must be a valid number with 2 decimal places")
    private Double amount;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    @NotNull(message = "Status must not be null")
    private PaymentStatus status; // PENDING, SUCCESS, FAILED

    @Enumerated(EnumType.STRING)
    @Column(name = "method", nullable = false, length = 20)
    @NotNull(message = "Method must not be null")
    private PaymentMethod method; // CARD, PAYPAL

    @Column(name = "created_at", nullable = false)
    @NotNull(message = "CreatedAt must not be null")
    private LocalDateTime createdAt;

    // automatically set the time and status
    @PrePersist
    public void prePersist() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (status == null) {
            status = PaymentStatus.PENDING;
        }
    }
}
