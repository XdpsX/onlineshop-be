package com.xdpsx.ecommerce.repositories.criteria;

import com.xdpsx.ecommerce.entities.Vendor;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public class VendorCriteriaRepositoryImpl implements VendorCriteriaRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Vendor> findWithFilter(Pageable pageable, String name, String sortField) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Vendor> query = cb.createQuery(Vendor.class);
        Root<Vendor> root = query.from(Vendor.class);

        if (name != null && !name.isEmpty()) {
            Predicate namePred = cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            query.where(namePred);
        }

        Order order = getSortOrder(sortField, cb, root);
        query.orderBy(order);
        TypedQuery<Vendor> typedQuery = entityManager.createQuery(query);
        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Vendor> vendors = typedQuery.getResultList();
        return new PageImpl<>(vendors, pageable, totalRows);
    }

    private Order getSortOrder(String sortField, CriteriaBuilder cb, Root<Vendor> root) {
        String actualField = sortField;
        if (sortField.startsWith("-")){
            actualField = sortField.substring(1);
        }

        if (actualField.equalsIgnoreCase("date")) {
            Expression<LocalDateTime> coalesceExpression = cb.coalesce(root.get("updatedAt"), root.get("createdAt"));
            return sortField.startsWith("-") ? cb.desc(coalesceExpression) : cb.asc(coalesceExpression);
        } else {
            return sortField.startsWith("-") ? cb.desc(root.get(actualField)) : cb.asc(root.get(actualField));
        }
    }
}
