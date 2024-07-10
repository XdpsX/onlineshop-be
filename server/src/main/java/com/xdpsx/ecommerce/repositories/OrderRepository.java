package com.xdpsx.ecommerce.repositories;

import com.xdpsx.ecommerce.entities.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
