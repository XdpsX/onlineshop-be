package com.xdpsx.ecommerce.dtos.product;

import com.xdpsx.ecommerce.dtos.common.AbstractPageParams;
import com.xdpsx.ecommerce.validator.SortFieldConstraint;
import jakarta.validation.constraints.Min;
import lombok.Data;

import static com.xdpsx.ecommerce.constants.AppConstants.DEFAULT_SORT_FIELD;

@Data
public class ProductPageParams extends AbstractPageParams {
    @SortFieldConstraint(sortFields = {"date", "name", "price"})
    private String sort = DEFAULT_SORT_FIELD;

    @Min(value = 1, message = "Min price must be greater than 0")
    private Double minPrice;
    @Min(value = 1, message = "Max price must be greater than 0")
    private Double maxPrice;
    private boolean hasDiscount;
    private Integer vendorId;
}
