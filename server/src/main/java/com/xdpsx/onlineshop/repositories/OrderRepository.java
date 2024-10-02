package com.xdpsx.onlineshop.repositories;

import com.xdpsx.onlineshop.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
