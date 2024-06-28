package com.xdpsx.ecommerce.dtos.common;

import jakarta.validation.constraints.Min;
import lombok.Data;

import static com.xdpsx.ecommerce.constants.AppConstants.*;

@Data
public abstract class AbstractPageRequest {
    @Min(value = 1, message = "Page number must be at least 1")
    private Integer pageNum = 1;

    @Min(value = MIN_ITEMS_PER_PAGE, message = "Page size must be at least " + MIN_ITEMS_PER_PAGE)
    private Integer pageSize = MIN_ITEMS_PER_PAGE;
    private String search;

}
