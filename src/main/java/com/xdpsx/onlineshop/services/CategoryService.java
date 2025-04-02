package com.xdpsx.onlineshop.services;

import java.util.List;
import java.util.Map;

import com.xdpsx.onlineshop.dtos.category.CategoryRequestDTO;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    CategoryResponse createCategory(CategoryRequestDTO request);

    CategoryResponse updateCategory(Integer id, CategoryRequestDTO request);

    void deleteCategory(Integer id);

    PageResponse<CategoryResponse> getCategoriesPage(PageParams params);

    Map<String, Boolean> checkExistsCat(String name, String slug);
}
