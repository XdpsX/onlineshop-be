package com.xdpsx.ecommerce.dtos.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PageResponse<T> {
    private Collection<T> items;
    private int pageNum;
    private int pageSize;
    private long totalItems;
    private int totalPages;
}
