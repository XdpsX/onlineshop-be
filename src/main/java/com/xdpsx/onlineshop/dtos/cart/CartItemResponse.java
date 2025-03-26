package com.xdpsx.onlineshop.dtos.cart;

import com.xdpsx.onlineshop.dtos.product.ProductResponse;

import lombok.Data;

@Data
public class CartItemResponse {
    private Integer quantity;
    private ProductResponse product;
}
