package com.xdpsx.onlineshop.services;

import java.util.List;
import java.util.Map;

import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.category.CreateCategoryDTO;
import com.xdpsx.onlineshop.dtos.category.UpdateCategoryDTO;
import com.xdpsx.onlineshop.dtos.common.ModifyExclusiveDTO;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;

public interface CategoryService {
    List<CategoryResponse> getAllCategories();

    CategoryResponse createCategory(CreateCategoryDTO request);

    CategoryResponse updateCategory(Integer id, UpdateCategoryDTO request);

    void deleteCategory(Integer id, ModifyExclusiveDTO request);

    PageResponse<CategoryResponse> getCategoriesPage(PageParams params);

    Map<String, Boolean> checkExistsCat(String name, String slug);
}
