package com.xdpsx.onlineshop.dtos.brand;

import com.xdpsx.onlineshop.dtos.common.AbstractPageParams;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class AdminBrandFilter extends AbstractPageParams {
    private String name;
    private Boolean publicFlg;
    private String sort;
}
