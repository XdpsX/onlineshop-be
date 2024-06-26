package com.xdpsx.ecommerce.specifications;

import com.xdpsx.ecommerce.entities.Vendor;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public class VendorSpecification {

    public static Specification<Vendor> hasName(String name) {
        return (Root<Vendor> root, CriteriaQuery<?> query, CriteriaBuilder builder) -> {
            if (name == null || name.isEmpty()) {
                return builder.conjunction();
            }
            return builder.like(builder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    public static Specification<Vendor> sortByDateDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(criteriaBuilder.coalesce(root.get("updatedAt"), root.get("createdAt"))));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Vendor> sortByDateAsc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(criteriaBuilder.coalesce(root.get("updatedAt"), root.get("createdAt"))));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Vendor> sortByNameAsc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.asc(root.get("name")));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Vendor> sortByNameDesc() {
        return (root, query, criteriaBuilder) -> {
            query.orderBy(criteriaBuilder.desc(root.get("name")));
            return criteriaBuilder.conjunction();
        };
    }

    public static Specification<Vendor> buildSearchSpecification(String name, String sort) {
        Specification<Vendor> spec = Specification.where(hasName(name));
        switch (sort) {
            case "+name", "name":
                spec = spec.and(VendorSpecification.sortByNameAsc());
                break;
            case "-name":
                spec = spec.and(VendorSpecification.sortByNameDesc());
                break;
            case "+date", "date":
                spec = spec.and(VendorSpecification.sortByDateAsc());
                break;
            default:
                spec = spec.and(VendorSpecification.sortByDateDesc());
                break;
        }
        return spec;
    }
}
