package com.xdpsx.onlineshop.repositories.specs;

import static com.xdpsx.onlineshop.repositories.common.SearchOperator.IS_NULL;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Predicate;

import org.springframework.data.jpa.domain.Specification;

import com.xdpsx.onlineshop.repositories.common.SearchCriteria;

public abstract class BaseSpecification<T> {

    public Specification<T> build(List<SearchCriteria> criteriaList) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            for (SearchCriteria criteria : criteriaList) {
                if (criteria.getValue() == null && !criteria.getOperator().equals(IS_NULL)) continue;

                switch (criteria.getOperator()) {
                    case EQUAL:
                        predicates.add(cb.equal(root.get(criteria.getKey()), criteria.getValue()));
                        break;
                    case LIKE:
                        predicates.add(cb.like(
                                cb.lower(root.get(criteria.getKey())),
                                "%" + criteria.getValue().toString().toLowerCase() + "%"));
                        break;
                    case GREATER_THAN:
                        predicates.add(cb.greaterThan(root.get(criteria.getKey()), (Comparable) criteria.getValue()));
                        break;
                    case LESS_THAN:
                        predicates.add(cb.lessThan(root.get(criteria.getKey()), (Comparable) criteria.getValue()));
                        break;
                    case GREATER_THAN_EQUAL:
                        predicates.add(
                                cb.greaterThanOrEqualTo(root.get(criteria.getKey()), (Comparable) criteria.getValue()));
                        break;
                    case LESS_THAN_EQUAL:
                        predicates.add(
                                cb.lessThanOrEqualTo(root.get(criteria.getKey()), (Comparable) criteria.getValue()));
                        break;
                    case IN:
                        predicates.add(root.get(criteria.getKey()).in((Collection<?>) criteria.getValue()));
                        break;
                    case IS_NULL:
                        predicates.add(cb.isNull(root.get(criteria.getKey())));
                        break;
                }
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }

    /**
     * Sort by any field dynamically
     * @param field the field to sort by (e.g. "name", "createdAt", "updatedAt")
     * @param asc boolean indicating whether the sorting is ascending or descending
     * @return Specification
     */
    public Specification<T> sortByField(String field, boolean asc) {
        return (root, query, criteriaBuilder) -> {
            if (asc) {
                query.orderBy(criteriaBuilder.asc(root.get(field)));
            } else {
                query.orderBy(criteriaBuilder.desc(root.get(field)));
            }
            return criteriaBuilder.conjunction();
        };
    }

    /**
     * Sort by any field dynamically
     * @param expression the expression to sort by, e.g: criteriaBuilder.coalesce(root.get(field), root.get("createdAt"));
     * @param asc boolean indicating whether the sorting is ascending or descending
     * @return Specification
     */
    public Specification<T> sortByField(Expression<?> expression, boolean asc) {
        return (root, query, criteriaBuilder) -> {
            if (asc) {
                query.orderBy(criteriaBuilder.asc(expression));
            } else {
                query.orderBy(criteriaBuilder.desc(expression));
            }
            return criteriaBuilder.conjunction();
        };
    }

    public Specification<T> sortByAuditDate(boolean asc) {
        return (root, query, criteriaBuilder) -> {
            Expression<?> sortExpression = criteriaBuilder.coalesce(root.get("updatedAt"), root.get("createdAt"));
            return sortByField(sortExpression, asc).toPredicate(root, query, criteriaBuilder);
        };
    }
}
