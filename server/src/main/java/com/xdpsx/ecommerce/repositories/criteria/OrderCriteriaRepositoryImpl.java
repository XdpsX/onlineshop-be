package com.xdpsx.ecommerce.repositories.criteria;

import com.xdpsx.ecommerce.entities.Order;
import com.xdpsx.ecommerce.entities.OrderStatus;
import com.xdpsx.ecommerce.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class OrderCriteriaRepositoryImpl implements OrderCriteriaRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Order> findWithFilters(Pageable pageable, String trackingNumber, String sort, OrderStatus status, Long userId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = cb.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);

        List<Predicate> predicates = new ArrayList<>();
        if (trackingNumber != null && !trackingNumber.isEmpty()) {
            predicates.add(cb.like(root.get("trackingNumber"), trackingNumber));
        }
        if (status != null){
            predicates.add(cb.equal(root.get("status"), status.name()));
        }
        if (userId != null) {
            predicates.add(cb.equal(root.get("user").get("id"), userId));
        }
        query.where(predicates.toArray(new Predicate[0]));

        return null;
    }
}
