package com.xdpsx.onlineshop.mappers;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.xdpsx.onlineshop.dtos.brand.BrandResponse;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.dtos.product.ProductResponse;
import com.xdpsx.onlineshop.entities.Brand;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.entities.Product;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PageMapper {
    private final CategoryMapper categoryMapper;
    private final BrandMapper brandMapper;
    private final ProductMapper productMapper;

    public PageResponse<CategoryResponse> toCategoryPageResponse(Page<Category> categoryPage) {
        return toPageResponse(categoryPage, categoryMapper::fromEntityToResponse);
    }

    public PageResponse<BrandResponse> toBrandPageResponse(Page<Brand> brandPage) {
        return toPageResponse(brandPage, brandMapper::fromEntityToResponse);
    }

    public PageResponse<ProductResponse> toProductPageResponse(Page<Product> productPage) {
        return toPageResponse(productPage, productMapper::fromEntityToResponse);
    }

    public <T, R> PageResponse<R> toPageResponse(Page<T> page, Function<T, R> mapper) {
        List<R> responses = page.getContent().stream().map(mapper).collect(Collectors.toList());
        return PageResponse.<R>builder()
                .items(responses)
                .pageNum(page.getNumber() + 1)
                .pageSize(page.getSize())
                .totalItems(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }
}
