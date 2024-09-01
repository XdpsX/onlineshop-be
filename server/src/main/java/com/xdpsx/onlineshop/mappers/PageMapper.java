package com.xdpsx.onlineshop.mappers;

import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.entities.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class PageMapper {
    private final CategoryMapper categoryMapper;

    public PageResponse<CategoryResponse> toCategoryPageResponse(Page<Category> categoryPage){
        return toPageResponse(categoryPage, categoryMapper::fromEntityToResponse);
    }

    public <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper) {
        List<R> responses = page.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());
        return PageResponse.<R>builder()
                .items(responses)
                .pageNum(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
