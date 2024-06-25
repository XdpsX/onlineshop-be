package com.xdpsx.ecommerce.dtos.product;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.xdpsx.ecommerce.dtos.category.CategoryResponse;
import com.xdpsx.ecommerce.dtos.vendor.VendorResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductResponse {
    private Long id;

    private String name;

    private double price;

    private double discountPercent;

    private boolean inStock;

    private boolean enabled;

    private String description;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String mainImage;

    private List<ProductImageDTO> images;

    private CategoryResponse category;

    private VendorResponse vendor;
}
