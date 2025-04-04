package com.xdpsx.onlineshop.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.category.CreateCategoryDTO;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.exceptions.NotFoundException;
import com.xdpsx.onlineshop.mappers.CategoryMapper;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.services.impl.CategoryServiceImpl;

@ExtendWith(MockitoExtension.class)
public class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @DisplayName("Create category successfully")
    @Test
    void testCreateCategory_ShouldSaveCategory() {
        CreateCategoryDTO request =
                CreateCategoryDTO.builder().name("Test Category").build();
        Category category = Category.builder().name("Test Category").build();
        Category savedCategory = Category.builder().id(1).name("Test Category").build();
        CategoryResponse expectedResponse = CategoryResponse.builder()
                .id(1)
                .name("Test Category")
                .slug("test-category")
                .build();

        when(categoryRepository.existsByName(request.getName())).thenReturn(false);
        when(categoryMapper.fromRequestToEntity(request)).thenReturn(category);
        when(categoryRepository.save(any(Category.class))).thenReturn(savedCategory);
        when(categoryMapper.fromEntityToResponse(savedCategory)).thenReturn(expectedResponse);

        CategoryResponse actualResponse = categoryService.createCategory(request);

        assertEquals(expectedResponse, actualResponse);
        verify(categoryRepository).save(any(Category.class));
    }

    @DisplayName("Create category with existing name")
    //    @Test
    void testCreateCategory_WhenCategoryAlreadyExists_ShouldThrowBadRequestException() {
        CreateCategoryDTO request =
                CreateCategoryDTO.builder().name("Existing Category").build();

        when(categoryRepository.existsByName(request.getName())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> categoryService.createCategory(request));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @DisplayName("Update category successfully")
    @Test
    void testUpdateCategory_ShouldUpdateCategory() {
        Integer categoryId = 1;
        CreateCategoryDTO request =
                CreateCategoryDTO.builder().name("Updated Category").build();
        Category existingCategory =
                Category.builder().id(1).name("Old Category").build();
        Category updatedCategory =
                Category.builder().id(1).name("Updated Category").build();
        CategoryResponse expectedResponse = CategoryResponse.builder()
                .id(1)
                .name("Updated Category")
                .slug("updated-category")
                .build();

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
        CreateCategoryDTO request =
                CreateCategoryDTO.builder().name("Updated Category").build();

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.updateCategory(categoryId, request));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @DisplayName("Update category with existing name")
    //    @Test
    void testUpdateCategory_WhenCategoryWithSameNameExists_ShouldThrowBadRequestException() {
        Integer categoryId = 1;
        CreateCategoryDTO request =
                CreateCategoryDTO.builder().name("Existing Category").build();
        Category existingCategory =
                Category.builder().id(1).name("Old Category").build();

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(existingCategory));
        when(categoryRepository.existsByName(request.getName())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> categoryService.updateCategory(categoryId, request));
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @DisplayName("Delete category successfully")
    @Test
    void testDeleteCategory_ShouldDeleteCategory() {
        Integer categoryId = 1;
        Category existingCategory =
                Category.builder().id(1).name("Category To Delete").build();

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(existingCategory));
        when(categoryRepository.countCategoriesInOtherTables(existingCategory.getId()))
                .thenReturn(0L);

        categoryService.deleteCategory(categoryId);

        verify(categoryRepository).delete(existingCategory);
    }

    @DisplayName("Delete not found category")
    @Test
    void testDeleteCategory_WhenCategoryNotFound_ShouldThrowResourceNotFoundException() {
        Integer categoryId = 1;

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.empty());

        assertThrows(NotFoundException.class, () -> categoryService.deleteCategory(categoryId));
        verify(categoryRepository, never()).delete(any(Category.class));
    }

    @DisplayName("Delete category that is currently in use")
    @Test
    void testDeleteCategory_WhenCategoryInUser_ShouldThrowBadRequestException() {
        Integer categoryId = 1;
        String msg = "Category is in use";
        Category existingCategory =
                Category.builder().id(1).name("Category To Delete").build();

        when(categoryRepository.findById(categoryId)).thenReturn(java.util.Optional.of(existingCategory));
        when(categoryRepository.countCategoriesInOtherTables(existingCategory.getId()))
                .thenReturn(1L);

        BadRequestException exception =
                assertThrows(BadRequestException.class, () -> categoryService.deleteCategory(categoryId));

        assertEquals(msg, exception.getMessage());
        verify(categoryRepository, never()).delete(existingCategory);
    }
}
