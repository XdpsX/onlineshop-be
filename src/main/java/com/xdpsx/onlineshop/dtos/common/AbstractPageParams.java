package com.xdpsx.onlineshop.dtos.common;

import static com.xdpsx.onlineshop.constants.PageConstants.*;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractPageParams {
    @Min(value = 1)
    private Integer pageNum = 1;

    @Min(value = MIN_ITEMS_PER_PAGE)
    @Max(value = MAX_ITEMS_PER_PAGE)
    private Integer pageSize = DEFAULT_ITEMS_PER_PAGE;

}
