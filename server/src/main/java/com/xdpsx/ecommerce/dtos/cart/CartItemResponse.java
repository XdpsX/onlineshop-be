package com.xdpsx.ecommerce.dtos.cart;

import com.xdpsx.ecommerce.dtos.product.ProductResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Integer quantity;
    private ProductResponse product;
}
