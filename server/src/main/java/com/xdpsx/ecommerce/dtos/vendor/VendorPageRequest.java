package com.xdpsx.ecommerce.dtos.vendor;

import com.xdpsx.ecommerce.dtos.common.PageRequest;
import lombok.Data;
import lombok.ToString;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.xdpsx.ecommerce.constants.AppConstants.DEFAULT_SORT_FIELD;

@Data
public class VendorPageRequest extends PageRequest {
    @ToString.Exclude
    private final static Set<String> SORT_FILEDS = new HashSet<>(Arrays.asList("date", "name"));

    @Override
    public void setSort(String sort) {
        String actualField = sort;
        if (sort.startsWith("+") || sort.startsWith("-")){
            actualField = sort.substring(1);
        }

        if (!SORT_FILEDS.contains(actualField)){
            super.setSort(DEFAULT_SORT_FIELD);
            return;
        }
        super.setSort(sort);
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
