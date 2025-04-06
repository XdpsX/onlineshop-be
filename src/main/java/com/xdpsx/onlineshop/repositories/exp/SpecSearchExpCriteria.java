package com.xdpsx.onlineshop.repositories.exp;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.xdpsx.onlineshop.repositories.exp.SearchExpOperation.*;

@Getter
@Setter
@NoArgsConstructor
public class SpecSearchExpCriteria {

    private String key;
    private SearchExpOperation operation;
    private Object value;
    private boolean orPredicate;


    public SpecSearchExpCriteria(final String key, final SearchExpOperation operation, final Object value) {
        super();
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchExpCriteria(final String orPredicate, final String key, final SearchExpOperation operation, final Object value) {
        super();
        this.orPredicate = orPredicate != null && orPredicate.equals(OR_PREDICATE_FLAG);
        this.key = key;
        this.operation = operation;
        this.value = value;
    }

    public SpecSearchExpCriteria(String key, String operation, String prefix, String value, String suffix) {
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
        }
        this.key = key;
        this.operation = searchExpOperation;
        this.value = value;
    }

}
