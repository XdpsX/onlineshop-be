package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.request.CategoryRequest;
import com.xdpsx.onlineshop.dtos.response.CategoryResponse;

import java.util.List;

public interface CategoryService {
    List<CategoryResponse> listCategories();
    CategoryResponse createCategory(CategoryRequest request);
    CategoryResponse updateCategory(Integer id, CategoryRequest request);
    void deleteCategory(Integer id);
}
