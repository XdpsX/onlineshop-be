package com.xdpsx.onlineshop.dtos.common;

import com.xdpsx.onlineshop.validations.SortConstraint;
import lombok.*;

import static com.xdpsx.onlineshop.constants.FieldConstants.*;
import static com.xdpsx.onlineshop.constants.PageConstants.DEFAULT_SORT_FIELD;

@Setter @Getter @NoArgsConstructor @AllArgsConstructor @Builder
public class PageParams extends AbstractPageParams {
    @SortConstraint(fields = {FIELD_NAME, FIELD_DATE})
    private String sort = DEFAULT_SORT_FIELD;
}