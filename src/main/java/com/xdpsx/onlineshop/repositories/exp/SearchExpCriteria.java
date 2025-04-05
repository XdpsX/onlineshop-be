package com.xdpsx.onlineshop.repositories.exp;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SearchExpCriteria {
    private String key; // column
    private String operation; // =, <, >,...
    private Object value;

}
