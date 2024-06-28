package com.xdpsx.ecommerce.dtos.product;

import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ProductRequest {
    @NotBlank(message = "Product name is required")
    @Size(max = 255, message = "Product name cannot exceed 255 characters")
    private String name;

    @Min(value = 0, message = "Product price must be at least 0")
    @Max(value = 1_000_000_000, message = "Product price cannot exceed 1.000.000.000")
    private double price;

    private double discountPercent;

    private boolean inStock;

    private boolean enabled;

    @Size(max=4096, message = "Product description cannot exceed 4096 characters")
    private String description;

    @NotNull(message = "Category id can not be null")
    private Integer categoryId;

    @NotNull(message = "Vendor id can not be null")
    private Integer vendorId;
}
