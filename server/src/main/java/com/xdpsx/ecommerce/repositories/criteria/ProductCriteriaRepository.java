package com.xdpsx.ecommerce.repositories.criteria;

import com.xdpsx.ecommerce.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductCriteriaRepository {
    Page<Product> findWithFilters(
            Pageable pageable,
            String name,
            String sort,
            Boolean enabled,
            Double minPrice,
            Double maxPrice,
            boolean hasDiscount,
            Integer vendorId,
            Integer categoryId
    );
}
