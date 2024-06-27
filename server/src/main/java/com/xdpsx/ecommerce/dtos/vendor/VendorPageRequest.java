package com.xdpsx.ecommerce.dtos.vendor;

import com.xdpsx.ecommerce.dtos.common.PageRequest;
import com.xdpsx.ecommerce.validator.SortFieldConstraint;
import lombok.Data;

import static com.xdpsx.ecommerce.constants.AppConstants.DEFAULT_SORT_FIELD;

@Data
public class VendorPageRequest extends PageRequest {
    @SortFieldConstraint(sortFields = {"date", "name"})
    private String sort = DEFAULT_SORT_FIELD;

}
