package com.xdpsx.onlineshop.repositories.specs;

import static com.xdpsx.onlineshop.constants.FieldConstants.FIELD_DATE;
import static com.xdpsx.onlineshop.constants.FieldConstants.FIELD_NAME;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.JoinType;

import org.springframework.data.jpa.domain.Specification;

import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.repositories.common.SearchCriteria;
import com.xdpsx.onlineshop.repositories.common.SearchOperator;

public class CategorySpecification extends BaseSpecification<Category> {
    private static CategorySpecification instance;

    public static CategorySpecification getInstance() {
        if (instance == null) {
            instance = new CategorySpecification();
        }
        return instance;
    }

    public Specification<Category> buildAdminCategoriesSpec(
            String name, Boolean publicFlg, String sort, Integer level) {
        List<SearchCriteria> criteriaList = new ArrayList<>();

        if (name != null && !name.isBlank()) {
            criteriaList.add(new SearchCriteria("name", name, SearchOperator.LIKE));
        }

        if (publicFlg != null) {
            criteriaList.add(new SearchCriteria("publicFlg", publicFlg, SearchOperator.EQUAL));
        }

        Specification<Category> spec = build(criteriaList);

        spec = applySort(spec, sort);

        if (level != null) {
            spec = spec.and(levelEquals(level));
        }

        return spec;
    }

    public Specification<Category> buildCategoryTreeSpec(Category parent, String sort) {
        List<SearchCriteria> criteriaList =
                new ArrayList<>(List.of(new SearchCriteria("publicFlg", true, SearchOperator.EQUAL)));

        if (parent == null) {
            criteriaList.add(new SearchCriteria("parent", null, SearchOperator.IS_NULL));
        } else {
            criteriaList.add(new SearchCriteria("parent", parent, SearchOperator.EQUAL));
        }

        Specification<Category> spec = build(criteriaList);

        spec = applySort(spec, sort);

        return spec;
    }

    private Specification<Category> levelEquals(Integer level) {
        return (root, query, cb) -> {
            if (level == null) return null;
            root.fetch("parent", JoinType.LEFT);
            return switch (level) {
                case 1 -> cb.isNull(root.get("parent"));
                case 2 -> cb.and(
                        cb.isNotNull(root.get("parent")),
                        cb.isNull(root.get("parent").get("parent")));
                case 3 -> cb.and(
                        cb.isNotNull(root.get("parent")),
                        cb.isNotNull(root.get("parent").get("parent")),
                        cb.isNull(root.get("parent").get("parent").get("parent")));
                default -> throw new IllegalArgumentException("Only levels 1 to 3 are supported.");
            };
        };
    }

    private Specification<Category> applySort(Specification<Category> spec, String sort) {
        if (sort != null && !sort.isBlank()) {
            boolean asc = !sort.startsWith("-");
            String sortField = asc ? sort : sort.substring(1);
            spec = switch (sortField) {
                case FIELD_NAME -> spec.and(sortByField(FIELD_NAME, asc));
                case FIELD_DATE -> spec.and(sortByAuditDate(asc));
                default -> throw new IllegalStateException("Unexpected value: " + sortField);};
        }
        return spec;
    }
}
