package com.kashkina.bookstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartDTO {

    private Long id;

    @NotNull(message = "User ID must not be null")
    private Long userId;
}
