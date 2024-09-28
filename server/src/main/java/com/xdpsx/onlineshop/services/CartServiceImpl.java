package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.cart.CartItemRequest;
import com.xdpsx.onlineshop.dtos.cart.CartItemResponse;
import com.xdpsx.onlineshop.entities.CartItem;
import com.xdpsx.onlineshop.entities.Product;
import com.xdpsx.onlineshop.entities.User;
import com.xdpsx.onlineshop.entities.ids.CartItemId;
import com.xdpsx.onlineshop.exceptions.ResourceNotFoundException;
import com.xdpsx.onlineshop.mappers.CartItemMapper;
import com.xdpsx.onlineshop.repositories.CartItemRepository;
import com.xdpsx.onlineshop.repositories.ProductRepository;
import com.xdpsx.onlineshop.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartItemMapper cartItemMapper;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Override
    public CartItemResponse addToCart(String userEmail, CartItemRequest request) {
        User user = getUser(userEmail);
        Product product = getProduct(request);
        CartItemId cartItemId = new CartItemId(user.getId(), product.getId());
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElse(null);
        if (cartItem == null) {
            CartItem newCartItem = CartItem.builder()
                    .id(cartItemId)
                    .quantity(request.getQuantity())
                    .user(user)
                    .product(product)
                    .build();
             return cartItemMapper.fromEntityToResponse(cartItemRepository.save(newCartItem));
        }else {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
            return cartItemMapper.fromEntityToResponse(cartItemRepository.save(cartItem));
        }
    }

    @Override
    public void removeCartItem(String userEmail, Long productId) {
        User user = getUser(userEmail);
        CartItemId cartItemId = CartItemId.builder()
                .productId(productId)
                .userId(user.getId())
                .build();
        CartItem cartItem = getCartItem(cartItemId);
        cartItemRepository.delete(cartItem);
    }

    private CartItem getCartItem(CartItemId cartItemId) {
        return cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Can not found Cart item"));
    }

    @Override
    public List<CartItemResponse> getCart(String userEmail) {
        User user = getUser(userEmail);
        List<CartItem> cartItems = cartItemRepository.findNewestByUserId(user.getId());
        return cartItems.stream()
                .map(cartItemMapper::fromEntityToResponse)
                .toList();
    }

    @Override
    public CartItemResponse updateCartItem(String userEmail, CartItemRequest request) {
        User user = getUser(userEmail);
        CartItemId cartItemId = new CartItemId(user.getId(), request.getProductId());
        CartItem cartItem = getCartItem(cartItemId);
        cartItem.setQuantity(request.getQuantity());
        return cartItemMapper.fromEntityToResponse(cartItemRepository.save(cartItem));
    }

    @Override
    public long countCartItems(String userEmail) {
        User user = getUser(userEmail);
        return cartItemRepository.countByUserId(user.getId());
    }

    private Product getProduct(CartItemRequest request) {
        return productRepository.findProductById(request.getProductId())
                .orElseThrow(() -> new ResourceNotFoundException("Product with id=%s not found!".formatted(request.getProductId())));
    }

    private User getUser(String userEmail) {
        return userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User with email=%s not found".formatted(userEmail)));
    }
}
