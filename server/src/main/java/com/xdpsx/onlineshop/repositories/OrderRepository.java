package com.xdpsx.onlineshop.repositories;

import com.xdpsx.onlineshop.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId " +
            "ORDER BY o.createdAt DESC")
    Page<Order> findByUser(Long userId, Pageable pageable);

    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = :id")
    Optional<Order> findById(Long id);

    @Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.user.id = :userId AND o.trackingNumber = :trackingNumber")
    Optional<Order> findByUserIdAndTrackingNumber(Long userId, String trackingNumber);

}
