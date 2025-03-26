package com.xdpsx.onlineshop.controllers;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xdpsx.onlineshop.dtos.brand.BrandNoCatsDTO;
import com.xdpsx.onlineshop.dtos.category.CategoryRequest;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.dtos.product.ProductResponse;
import com.xdpsx.onlineshop.services.BrandService;
import com.xdpsx.onlineshop.services.CategoryService;
import com.xdpsx.onlineshop.services.ProductService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;
    private final BrandService brandService;
    private final ProductService productService;

    @GetMapping("/filters")
    public ResponseEntity<PageResponse<CategoryResponse>> getCategoriesByPage(@Valid PageParams params) {
        PageResponse<CategoryResponse> pageResponse = categoryService.listCategoriesByPage(params);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }

    @GetMapping("/get-all")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.listAllCategories());
    }

    @GetMapping("/{categorySlug}")
    public ResponseEntity<CategoryResponse> getCategoryBySlug(@PathVariable("categorySlug") String categorySlug) {
        return ResponseEntity.ok(categoryService.getCategoryBySlug(categorySlug));
    }

    @GetMapping("/{categoryId}/brands")
    public ResponseEntity<List<BrandNoCatsDTO>> getBrandsByCategoryId(@PathVariable Integer categoryId) {
        return ResponseEntity.ok(brandService.listBrandsByCategoryId(categoryId));
    }

    @PostMapping("/create")
    public ResponseEntity<CategoryResponse> createCategory(@Valid @RequestBody CategoryRequest request) {
        CategoryResponse createdCategory = categoryService.createCategory(request);
        return new ResponseEntity<>(createdCategory, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Integer id, @Valid @RequestBody CategoryRequest request) {
        CategoryResponse updatedCategory = categoryService.updateCategory(id, request);
        return ResponseEntity.ok(updatedCategory);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Map<String, Boolean>> checkExistsCat(@RequestParam String name, @RequestParam String slug) {
        Map<String, Boolean> exists = categoryService.checkExistsCat(name, slug);
        return ResponseEntity.ok(exists);
    }

    @GetMapping("/{categoryId}/products")
    public ResponseEntity<PageResponse<ProductResponse>> getProductsByCategory(
            @PathVariable Integer categoryId,
            @RequestParam(defaultValue = "1") int pageNum,
            @RequestParam(defaultValue = "8") int pageSize,
            @RequestParam(required = false) List<Integer> brandIds,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {
        PageResponse<ProductResponse> response = productService.getProductsByCategoryId(
                categoryId, pageNum, pageSize, brandIds, sort, minPrice, maxPrice);
        return ResponseEntity.ok(response);
    }
}
