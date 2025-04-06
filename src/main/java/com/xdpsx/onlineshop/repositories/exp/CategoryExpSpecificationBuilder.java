package com.xdpsx.onlineshop.repositories.exp;

import com.xdpsx.onlineshop.entities.Category;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.xdpsx.onlineshop.repositories.exp.SearchExpOperation.*;

public class CategoryExpSpecificationBuilder {
    public final List<SpecSearchExpCriteria> params;

    public CategoryExpSpecificationBuilder() {
        params = new ArrayList<>();
    }

    // API
    public CategoryExpSpecificationBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public CategoryExpSpecificationBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
        SearchExpOperation searchExpOperation = SearchExpOperation.getSimpleOperation(operation.charAt(0));
        if (searchExpOperation != null) {
            if (searchExpOperation == EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    searchExpOperation = CONTAINS;
                } else if (startWithAsterisk) {
                    searchExpOperation = ENDS_WITH;
                } else if (endWithAsterisk) {
                    searchExpOperation = STARTS_WITH;
                }
            }
            params.add(new SpecSearchExpCriteria(orPredicate, key, searchExpOperation, value));
        }
        return this;
    }

    public Specification<Category> build() {
        if (params.isEmpty())
            return null;

        Specification<Category> result = new CategoryExpSpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new CategoryExpSpecification(params.get(i)))
                    : Specification.where(result).and(new CategoryExpSpecification(params.get(i)));
        }

        return result;
    }

    public CategoryExpSpecificationBuilder with(CategoryExpSpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public CategoryExpSpecificationBuilder with(SpecSearchExpCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
