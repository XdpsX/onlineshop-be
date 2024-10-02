package com.xdpsx.onlineshop.dtos.order;

import com.xdpsx.onlineshop.dtos.product.ProductResponse;
import lombok.Data;

@Data
public class OrderItemResponse {
    private Long id;
    private ProductResponse product;
    private Integer quantity;
}
