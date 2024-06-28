package com.xdpsx.ecommerce.dtos.common;

import com.xdpsx.ecommerce.validator.SortFieldConstraint;
import lombok.Data;

import static com.xdpsx.ecommerce.constants.AppConstants.DEFAULT_SORT_FIELD;

@Data
public class PageParams extends AbstractPageParams {
    @SortFieldConstraint(sortFields = {"date", "name"})
    private String sort = DEFAULT_SORT_FIELD;
}
