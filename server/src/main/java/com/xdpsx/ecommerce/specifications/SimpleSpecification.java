package com.xdpsx.ecommerce.specifications;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class SimpleSpecification<T> {
    public Specification<T> getSearchSpec(String name, String sort) {
        Specification<T> spec = Specification.where(hasName(name));
        switch (sort) {
            case "name":
                spec = spec.and(sortByNameAsc());
                break;
            case "-name":
                spec = spec.and(sortByNameDesc());
                break;
            case "date":
                spec = spec.and(sortByDateAsc());
                break;
            default:
                spec = spec.and(sortByDateDesc());
                break;
        }
        return spec;
    }

    public Specification<T> hasName(String name) {
        return (Root<T> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (name == null || name.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public Specification<T> sortByDateDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(criteriaBuilder.coalesce(root.get("updatedAt"), root.get("createdAt"))));
            return criteriaBuilder.conjunction();
        };
    }

    public Specification<T> sortByDateAsc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(criteriaBuilder.coalesce(root.get("updatedAt"), root.get("createdAt"))));
            return criteriaBuilder.conjunction();
        };
    }

    public Specification<T> sortByNameAsc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("name")));
            return criteriaBuilder.conjunction();
        };
    }

    public Specification<T> sortByNameDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("name")));
            return criteriaBuilder.conjunction();
        };
    }

}
