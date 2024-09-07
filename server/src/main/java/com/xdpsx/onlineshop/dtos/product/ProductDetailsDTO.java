package com.xdpsx.onlineshop.dtos.product;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
public class ProductDetailsDTO extends ProductResponse{
    private String description;
    private List<ProductImageDTO> images;
}
