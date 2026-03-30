package com.kashkina.bookstore.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.*;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "carts",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_cart_user", columnNames = "user_id")
        },
        indexes = {
                @Index(name = "idx_carts_user_id", columnList = "user_id")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @NotNull(message = "User ID must not be null")
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Builder.Default
    @OneToMany(
            mappedBy = "cart",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItem> items = new ArrayList<>();
}
