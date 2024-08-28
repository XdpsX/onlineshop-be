package com.xdpsx.ecommerce.dtos.order;

import com.xdpsx.ecommerce.dtos.common.AbstractPageParams;
import com.xdpsx.ecommerce.entities.OrderStatus;
import com.xdpsx.ecommerce.validator.SortFieldConstraint;
import lombok.Data;

import static com.xdpsx.ecommerce.constants.AppConstants.DEFAULT_SORT_FIELD;

@Data
public class OrderPageParams extends AbstractPageParams {
    @SortFieldConstraint(sortFields = {"date"})
    private String sort = DEFAULT_SORT_FIELD;

    private OrderStatus status;
}
