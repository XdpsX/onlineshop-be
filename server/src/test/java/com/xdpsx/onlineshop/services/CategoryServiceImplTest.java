package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.request.CategoryRequest;
import com.xdpsx.onlineshop.dtos.response.CategoryResponse;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.exceptions.ResourceNotFoundException;
import com.xdpsx.onlineshop.mappers.CategoryMapper;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.services.impl.CategoryServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    @Mock private CategoryRepository categoryRepository;
    @Mock private CategoryMapper categoryMapper;
    @InjectMocks private CategoryServiceImpl categoryService;

    @DisplayName("Create category successfully")
    @Test
    void testCreateCategory_ShouldSaveCategory() {
        CategoryRequest request = new CategoryRequest("Test Category");
        Category category = new Category(null, "Test Category", null);
        Category savedCategory = new Category(1, "Test Category", "test-category");
        CategoryResponse expectedResponse = new CategoryResponse(1, "Test Category", "test-category");

        when(categoryRepository.existsByName(request.getName())).thenReturn(false);
        when(categoryMapper.fromRequestToEntity(request)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.fromEntityToResponse(savedCategory)).thenReturn(expectedResponse);

        CategoryResponse actualResponse = categoryService.createCategory(request);

        assertEquals(expectedResponse, actualResponse);
        verify(categoryRepository).save(any(Category.class));
    }

    @DisplayName("Create category with existing name")
    @Test
    void testCreateCategory_WhenCategoryAlreadyExists_ShouldThrowBadRequestException() {
        CategoryRequest request = new CategoryRequest("Existing Category");

        when(categoryRepository.existsByName(request.getName())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> categoryService.createCategory(request));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @DisplayName("Update category successfully")
    @Test
    void testUpdateCategory_ShouldUpdateCategory() {
        Integer categoryId = 1;
        CategoryRequest request = new CategoryRequest("Updated Category");
        Category existingCategory = new Category(1, "Old Category", "old-category");
        Category updatedCategory = new Category(1, "Updated Category", "updated-category");
        CategoryResponse expectedResponse = new CategoryResponse(1, "Updated Category", "updated-category");

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(existingCategory));
        when(categoryRepository.existsByName(request.getName())).thenReturn(false);
        when(categoryRepository.save(existingCategory)).thenReturn(updatedCategory);
        when(categoryMapper.fromEntityToResponse(updatedCategory)).thenReturn(expectedResponse);

        CategoryResponse actualResponse = categoryService.updateCategory(categoryId, request);

        assertEquals(expectedResponse, actualResponse);
        verify(categoryRepository).save(existingCategory);
    }

    @DisplayName("Update not found category")
    @Test
    void testUpdateCategory_WhenCategoryNotFound_ShouldThrowResourceNotFoundException() {
        Integer categoryId = 1;
        CategoryRequest request = new CategoryRequest("Updated Category");

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.updateCategory(categoryId, request));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @DisplayName("Update category with existing name")
    @Test
    void testUpdateCategory_WhenCategoryWithSameNameExists_ShouldThrowBadRequestException() {
        Integer categoryId = 1;
        CategoryRequest request = new CategoryRequest("Existing Category");
        Category existingCategory = new Category(1, "Old Category", "old-category");

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(existingCategory));
        when(categoryRepository.existsByName(request.getName())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> categoryService.updateCategory(categoryId, request));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @DisplayName("Delete category successfully")
    @Test
    void testDeleteCategory_ShouldDeleteCategory() {
        Integer categoryId = 1;
        Category existingCategory = new Category(1, "Category To Delete", "category-to-delete");

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(existingCategory));

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).delete(existingCategory);
    }

    @DisplayName("Delete not found category")
    @Test
    void testDeleteCategory_WhenCategoryNotFound_ShouldThrowResourceNotFoundException() {
        Integer categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> categoryService.deleteCategory(categoryId));
        verify(categoryRepository, never()).delete(any(Category.class));
    }
}
