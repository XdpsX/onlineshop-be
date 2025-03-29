package com.xdpsx.onlineshop.controllers;

import java.util.List;
import java.util.Map;

import com.xdpsx.onlineshop.dtos.category.CreateCategoryDTO;
import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.xdpsx.onlineshop.constants.APIMessage;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.common.APIResponse;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.services.CategoryService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping("/categories")
    public APIResponse<PageResponse<CategoryResponse>> getCategoriesPage(@ParameterObject @Valid PageParams params) {
        PageResponse<CategoryResponse> data = categoryService.getCategoriesPage(params);
        return APIResponse.ok(data);
    }

    @GetMapping("/categories/get-all")
    public APIResponse<List<CategoryResponse>> getAllCategories() {
        List<CategoryResponse> data = categoryService.getAllCategories();
        return APIResponse.ok(data);
    }

    @GetMapping("/categories/exists")
    public APIResponse<Map<String, Boolean>> checkExistsCat(@RequestParam String name, @RequestParam String slug) {
        Map<String, Boolean> data = categoryService.checkExistsCat(name, slug);
        return APIResponse.ok(data);
    }

    //    @GetMapping("/{categoryId}/brands")
    //    public ResponseEntity<List<BrandNoCatsDTO>> getBrandsByCategoryId(@PathVariable Integer categoryId) {
    //        return ResponseEntity.ok(brandService.listBrandsByCategoryId(categoryId));
    //    }

    @PostMapping("/categories/create")
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<CategoryResponse> createCategory(@Valid @RequestBody CreateCategoryDTO request) {
        CategoryResponse data = categoryService.createCategory(request);
        return new APIResponse<>(HttpStatus.CREATED, data, APIMessage.CREATE_SUCCESSFUL);
    }

    @PutMapping("/categories/{id}/update")
    public APIResponse<CategoryResponse> updateCategory(
            @PathVariable Integer id, @Valid @RequestBody CreateCategoryDTO request) {
        CategoryResponse data = categoryService.updateCategory(id, request);
        return APIResponse.ok(data, APIMessage.UPDATE_SUCCESSFUL);
    }

    @DeleteMapping("/categories/{id}/delete")
    public APIResponse<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return APIResponse.noContent(APIMessage.DELETE_SUCCESSFUL);
    }

    //    @GetMapping("/categories/{categoryId}/products")
    //    public ResponseEntity<PageResponse<ProductResponse>> getProductsByCategory(
    //            @PathVariable Integer categoryId,
    //            @RequestParam(defaultValue = "1") int pageNum,
    //            @RequestParam(defaultValue = "8") int pageSize,
    //            @RequestParam(required = false) List<Integer> brandIds,
    //            @RequestParam(required = false) String sort,
    //            @RequestParam(required = false) Double minPrice,
    //            @RequestParam(required = false) Double maxPrice) {
    //        PageResponse<ProductResponse> response = productService.getProductsByCategoryId(
    //                categoryId, pageNum, pageSize, brandIds, sort, minPrice, maxPrice);
    //        return ResponseEntity.ok(response);
    //    }
}
