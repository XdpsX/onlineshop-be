package com.xdpsx.ecommerce.services.impl;

import com.xdpsx.ecommerce.dtos.cart.CartItemRequest;
import com.xdpsx.ecommerce.dtos.cart.CartItemResponse;
import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.entities.*;
import com.xdpsx.ecommerce.exceptions.ResourceNotFoundException;
import com.xdpsx.ecommerce.mappers.CartItemMapper;
import com.xdpsx.ecommerce.repositories.CartItemRepository;
import com.xdpsx.ecommerce.repositories.ProductRepository;
import com.xdpsx.ecommerce.services.CartItemService;
import com.xdpsx.ecommerce.specifications.CartSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartItemMapper cartItemMapper;

    private final CartSpecification spec;

    @Override
    public PageResponse<CartItemResponse> getCartItems(Long userId, Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        Page<CartItem> cartItemPage = cartItemRepository.findAll(
                spec.getFindCartSpec(userId),
                pageable
        );
        List<CartItemResponse> cartItems = cartItemPage.getContent().stream()
                .map(cartItemMapper::fromEntityToResponse)
                .collect(Collectors.toList());
        return PageResponse.<CartItemResponse>builder()
                .items(cartItems)
                .pageNum(cartItemPage.getNumber() + 1)
                .pageSize(cartItemPage.getSize())
                .totalItems(cartItemPage.getTotalElements())
                .totalPages(cartItemPage.getTotalPages())
                .build();
    }

    @Override
    public CartItemResponse addCartItem(CartItemRequest request, User user) {
        Product product = productRepository.findEnabledProductsById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id=%s not found!".formatted(request.getProductId())));
        CartItem cartItem = cartItemMapper.fromRequestToEntity(request);
        CartItemId cartItemId = CartItemId.builder()
                .productId(product.getId())
                .userId(user.getId())
                .build();
        cartItem.setId(cartItemId);
        cartItem.setProduct(product);
        cartItem.setUser(user);

        CartItem savedCartItem = cartItemRepository.save(cartItem);

        return cartItemMapper.fromEntityToResponse(savedCartItem);
    }

    @Override
    public void removeCartItem(Long productId, Long userId) {
        CartItemId cartItemId = CartItemId.builder()
                .productId(productId)
                .userId(userId)
                .build();
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Can not found Cart item"));
        cartItemRepository.delete(cartItem);
    }
}
