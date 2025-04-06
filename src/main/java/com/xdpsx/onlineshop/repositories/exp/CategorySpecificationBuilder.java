package com.xdpsx.onlineshop.repositories.exp;

import com.xdpsx.onlineshop.entities.Category;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

import static com.xdpsx.onlineshop.repositories.exp.SearchOperation.*;

public class CategorySpecificationBuilder {
    public final List<SpecSearchCriteria> params;

    public CategorySpecificationBuilder() {
        params = new ArrayList<>();
    }

    // API
    public CategorySpecificationBuilder with(final String key, final String operation, final Object value, final String prefix, final String suffix) {
        return with(null, key, operation, value, prefix, suffix);
    }

    public CategorySpecificationBuilder with(final String orPredicate, final String key, final String operation, final Object value, final String prefix, final String suffix) {
        SearchOperation searchOperation = SearchOperation.getSimpleOperation(operation.charAt(0));
        if (searchOperation != null) {
            if (searchOperation == EQUALITY) { // the operation may be complex operation
                final boolean startWithAsterisk = prefix != null && prefix.contains(ZERO_OR_MORE_REGEX);
                final boolean endWithAsterisk = suffix != null && suffix.contains(ZERO_OR_MORE_REGEX);

                if (startWithAsterisk && endWithAsterisk) {
                    searchOperation = CONTAINS;
                } else if (startWithAsterisk) {
                    searchOperation = ENDS_WITH;
                } else if (endWithAsterisk) {
                    searchOperation = STARTS_WITH;
                }
            }
            params.add(new SpecSearchCriteria(orPredicate, key, searchOperation, value));
        }
        return this;
    }

    public Specification<Category> build() {
        if (params.isEmpty())
            return null;

        Specification<Category> result = new CategorySpecification(params.get(0));

        for (int i = 1; i < params.size(); i++) {
            result = params.get(i).isOrPredicate()
                    ? Specification.where(result).or(new CategorySpecification(params.get(i)))
                    : Specification.where(result).and(new CategorySpecification(params.get(i)));
        }

        return result;
    }

    public CategorySpecificationBuilder with(CategorySpecification spec) {
        params.add(spec.getCriteria());
        return this;
    }

    public CategorySpecificationBuilder with(SpecSearchCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
