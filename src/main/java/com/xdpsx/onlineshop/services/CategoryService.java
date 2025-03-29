package com.xdpsx.onlineshop.services;

import java.util.List;
import java.util.Map;

import com.xdpsx.onlineshop.dtos.category.CategoryRequest;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;

public interface CategoryService {
    List<CategoryResponse> listAllCategories();

    CategoryResponse createCategory(CategoryRequest request);

    CategoryResponse updateCategory(Integer id, CategoryRequest request);

    void deleteCategory(Integer id);

    PageResponse<CategoryResponse> listCategoriesByPage(PageParams params);

    Map<String, Boolean> checkExistsCat(String name, String slug);
}
