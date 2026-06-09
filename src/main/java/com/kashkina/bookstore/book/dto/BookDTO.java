package com.kashkina.bookstore.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookDTO {

    private Long id;

    @NotBlank(message = "Title must not be blank")
    @Size(max = 100, message = "Title max length is 100")
    private String title;

    @NotBlank(message = "Author must not be blank")
    @Size(max = 100, message = "Author max length is 100")
    private String author;

    @Size(max = 1000, message = "Description max length is 1000")
    private String description;

    @NotNull(message = "Price must not be null")
    @DecimalMin(value = "0.0", message = "Price must be positive")
    private BigDecimal price;

    @NotNull(message = "Stock must not be null")
    @Min(value = 0, message = "Stock must be at least 0")
    private Integer stock;

    @Size(max = 50, message = "Category max length is 50")
    private String category;
}
