package com.shop.online.demo.model.dto;

import com.shop.online.demo.model.Product;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {

    @NotNull(message = "Name cannot be null")
    String name;

    @NotNull(message = "Price cannot be null")
    @Positive(message = "Price must be positive")
    BigDecimal price;

    @NotNull(message = "Quantity cannot be null")
    @Positive(message = "Quantity must be positive")
    Long quantity;

    public static Product toEntity(ProductDto productDto) {
        return toEntity(new Product(), productDto);
    }

    public static Product toEntity(Product product, ProductDto productDto) {
        return product
                .setName(productDto.getName())
                .setPrice(productDto.getPrice())
                .setQuantity(productDto.getQuantity());
    }
}
