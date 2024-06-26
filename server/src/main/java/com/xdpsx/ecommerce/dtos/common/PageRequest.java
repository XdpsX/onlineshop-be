package com.xdpsx.ecommerce.dtos.common;

import lombok.Data;

import static com.xdpsx.ecommerce.constants.AppConstants.*;

@Data
public class PageRequest {
    private int pageNum = 1;
    private int pageSize = 5;
    private String search;
    private String sort = DEFAULT_SORT_FIELD;

    public void setPageNum(int pageNum) {
        if (pageNum < 1){
            this.pageNum = 1;
            return;
        }
        this.pageNum = pageNum;
    }

    public void setPageSize(int pageSize) {
        if (pageSize < MIN_ITEMS_PER_PAGE) {
            this.pageSize = MIN_ITEMS_PER_PAGE;
            return;
        }
        if (pageSize > MAX_ITEMS_PER_PAGE){
            this.pageSize = MAX_ITEMS_PER_PAGE;
            return;
        }
        this.pageSize = pageSize;
    }
}
