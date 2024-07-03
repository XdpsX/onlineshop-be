package com.xdpsx.ecommerce.services;

import com.xdpsx.ecommerce.dtos.cart.CartItemRequest;
import com.xdpsx.ecommerce.dtos.cart.CartItemResponse;
import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.entities.User;

public interface CartItemService {
    PageResponse<CartItemResponse> getCartItems(Long userId, Integer pageNum, Integer pageSize);
    CartItemResponse addCartItem(CartItemRequest request, User user);
    void removeCartItem(Long productId, Long userId);
}
