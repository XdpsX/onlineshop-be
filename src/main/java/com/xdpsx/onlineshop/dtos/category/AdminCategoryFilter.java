package com.xdpsx.onlineshop.dtos.category;

import com.xdpsx.onlineshop.dtos.common.AbstractPageParams;

import lombok.*;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AdminCategoryFilter extends AbstractPageParams {
    private String name;
    private Boolean publicFlg;
    private String sort;
    private Integer level;
}
