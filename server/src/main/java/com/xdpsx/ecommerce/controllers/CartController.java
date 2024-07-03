package com.xdpsx.ecommerce.controllers;

import com.xdpsx.ecommerce.dtos.cart.CartItemRequest;
import com.xdpsx.ecommerce.dtos.cart.CartItemResponse;
import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.entities.User;
import com.xdpsx.ecommerce.services.CartItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartItemService cartItemService;

    @GetMapping
    public ResponseEntity<PageResponse<CartItemResponse>> getCart(
            @RequestParam(required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(required = false, defaultValue = "5") Integer pageSize
    ){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = (User) authentication.getPrincipal();
        PageResponse<CartItemResponse> cart = cartItemService.getCartItems(loggedUser.getId(), pageNum, pageSize);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/add")
    public ResponseEntity<CartItemResponse> addCartItem(@Valid @RequestBody CartItemRequest request){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = (User) authentication.getPrincipal();
        CartItemResponse response = cartItemService.addCartItem(request, loggedUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<Void> removeCartItem(@PathVariable Long productId){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User loggedUser = (User) authentication.getPrincipal();
        cartItemService.removeCartItem(productId, loggedUser.getId());
        return ResponseEntity.noContent().build();
    }
}
