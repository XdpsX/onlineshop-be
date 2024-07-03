package com.xdpsx.ecommerce.specifications;

import com.xdpsx.ecommerce.entities.CartItem;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class CartSpecification {
    public Specification<CartItem> getFindCartSpec(Long userId) {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(criteriaBuilder.coalesce(root.get("updatedAt"), root.get("createdAt"))));
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }
}
