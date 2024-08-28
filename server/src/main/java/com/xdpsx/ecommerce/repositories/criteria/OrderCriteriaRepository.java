package com.xdpsx.ecommerce.repositories.criteria;

import com.xdpsx.ecommerce.entities.Order;
import com.xdpsx.ecommerce.entities.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderCriteriaRepository {
    Page<Order> findWithFilters(
            Pageable pageable,
            String trackingNumber,
            String sort,
            OrderStatus status,
            Long userId
    );
}
