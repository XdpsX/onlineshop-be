package com.xdpsx.onlineshop.repositories;

import com.xdpsx.onlineshop.entities.CartItem;
import com.xdpsx.onlineshop.entities.ids.CartItemId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, CartItemId> {
    @Query(
            "SELECT ci FROM CartItem ci WHERE ci.user.id = :userId " +
            "ORDER BY ci.createdAt DESC"
    )
    List<CartItem> findNewestByUserId(@Param("userId") Long userId);

    @Query(
            "SELECT ci FROM CartItem ci WHERE ci.user.id = :userId AND ci.product.inStock = true " +
                    "ORDER BY ci.createdAt DESC"
    )
    List<CartItem> findInStockCartByUserId(@Param("userId") Long userId);

    long countByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("DELETE FROM CartItem ci WHERE ci.user.id = :userId AND ci.product.inStock = true")
    void deleteInStockCartByUserId(@Param("userId") Long userId);
}
