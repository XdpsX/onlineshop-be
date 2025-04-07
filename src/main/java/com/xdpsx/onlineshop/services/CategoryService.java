package com.xdpsx.onlineshop.services;

import java.util.List;

import com.xdpsx.onlineshop.dtos.category.*;
import com.xdpsx.onlineshop.dtos.common.CheckExistResponse;
import com.xdpsx.onlineshop.dtos.common.ModifyExclusiveDTO;
import com.xdpsx.onlineshop.dtos.common.PageResponse;

public interface CategoryService {
    PageResponse<AdminCategoryResponse> getAdminCategories(AdminCategoryFilter filter);

    AdminCategoryResponse getCategory(Integer categoryId);

    List<CategoryTreeResponse> getCategoryTree(CategoryTreeFilter filter);

    CategoryResponse createCategory(CreateCategoryRequest request);

    CheckExistResponse checkCategoryExist(CategoryExistRequest request);

    CategoryResponse updateCategory(Integer id, UpdateCategoryRequest request);

    void deleteCategory(Integer id, ModifyExclusiveDTO request);
}
