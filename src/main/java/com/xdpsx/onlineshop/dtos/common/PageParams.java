package com.xdpsx.onlineshop.dtos.common;

import static com.xdpsx.onlineshop.constants.FieldConstants.*;
import static com.xdpsx.onlineshop.constants.PageConstants.DEFAULT_SORT_FIELD;

import com.xdpsx.onlineshop.validations.SortConstraint;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageParams extends AbstractPageParams {
    private String search;

    @SortConstraint(fields = {FIELD_NAME, FIELD_DATE})
    @Parameter(description = FIELD_NAME + ", " + FIELD_DATE)
    @Builder.Default
    private String sort = DEFAULT_SORT_FIELD;
}
