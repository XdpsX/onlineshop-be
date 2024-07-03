package com.xdpsx.ecommerce.repositories;

import com.xdpsx.ecommerce.entities.CartItem;
import com.xdpsx.ecommerce.entities.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId>, JpaSpecificationExecutor<CartItem> {

}
