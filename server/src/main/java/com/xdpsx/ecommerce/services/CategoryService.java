package com.xdpsx.ecommerce.services;

import com.xdpsx.ecommerce.dtos.category.CategoryRequest;
import com.xdpsx.ecommerce.dtos.category.CategoryResponse;
import com.xdpsx.ecommerce.dtos.common.PageParams;
import com.xdpsx.ecommerce.dtos.common.PageResponse;

public interface CategoryService {
    PageResponse<CategoryResponse> getAllCategories(PageParams request);
    CategoryResponse getCategory(Integer id);
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Integer id, CategoryRequest request);
    void deleteCategory(Integer id);
}
