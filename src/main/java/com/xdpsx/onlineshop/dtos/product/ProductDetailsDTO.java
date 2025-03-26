package com.xdpsx.onlineshop.dtos.product;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data @SuperBuilder
public class ProductDetailsDTO extends ProductResponse{
    private String description;
    private List<ProductImageDTO> images;
}
