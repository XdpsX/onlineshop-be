package com.xdpsx.ecommerce.repositories.criteria;

import com.xdpsx.ecommerce.entities.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductCriteriaRepositoryImpl implements ProductCriteriaRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<Product> findWithFilters(Pageable pageable, String name, String sort, Boolean enabled,
                                         Double minPrice, Double maxPrice, boolean hasDiscount,
                                         Integer vendorId, Integer categoryId) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Product> query = cb.createQuery(Product.class);
        Root<Product> product = query.from(Product.class);

        List<Predicate> predicates = new ArrayList<>();
        if (name != null && !name.isEmpty()) {
            predicates.add(cb.like(product.get("name"), "%" + name + "%"));
        }
        if (enabled != null){
            predicates.add(cb.equal(product.get("enabled"), enabled));
        }
        if (minPrice != null) {
            predicates.add(cb.greaterThanOrEqualTo(product.get("price"), minPrice));
        }
        if (maxPrice != null) {
            predicates.add(cb.lessThanOrEqualTo(product.get("price"), maxPrice));
        }
        if (hasDiscount) {
            predicates.add(cb.greaterThan(product.get("discountPercent"), 0));
        }
        if (vendorId != null) {
            predicates.add(cb.equal(product.get("vendor").get("id"), vendorId));
        }
        if (categoryId != null) {
            predicates.add(cb.equal(product.get("category").get("id"), categoryId));
        }
        query.where(predicates.toArray(new Predicate[0]));

        Order order = getSortOrder(sort, cb, product);
        query.orderBy(order);
        TypedQuery<Product> typedQuery = entityManager.createQuery(query);
        int totalRows = typedQuery.getResultList().size();
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        List<Product> products = typedQuery.getResultList();
        return new PageImpl<>(products, pageable, totalRows);
    }

    private Order getSortOrder(String sortField, CriteriaBuilder cb, Root<Product> root) {
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
