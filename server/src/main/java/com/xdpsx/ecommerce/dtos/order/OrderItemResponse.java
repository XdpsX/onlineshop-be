package com.xdpsx.ecommerce.dtos.order;

import com.xdpsx.ecommerce.dtos.product.ProductResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class OrderItemResponse {
    private Long id;
    private ProductResponse product;
    private Integer quantity;
}
