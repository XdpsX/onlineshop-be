package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.category.CategoryCreateRequest;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> listCategories();
    CategoryResponse createCategory(CategoryCreateRequest request);
    CategoryResponse updateCategory(Integer id, CategoryCreateRequest request);
    void deleteCategory(Integer id);
}
