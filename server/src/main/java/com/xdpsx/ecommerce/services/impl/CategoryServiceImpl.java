package com.xdpsx.ecommerce.services.impl;

import com.xdpsx.ecommerce.dtos.category.CategoryRequest;
import com.xdpsx.ecommerce.dtos.category.CategoryResponse;
import com.xdpsx.ecommerce.entities.Category;
import com.xdpsx.ecommerce.exceptions.BadRequestException;
import com.xdpsx.ecommerce.exceptions.ResourceNotFoundException;
import com.xdpsx.ecommerce.mappers.CategoryMapper;
import com.xdpsx.ecommerce.repositories.CategoryRepository;
import com.xdpsx.ecommerce.services.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=[%s] not found!".formatted(id)));
        return categoryMapper.fromEntityToResponse(category);
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        Category category = categoryMapper.fromRequestToEntity(request);
        if (categoryRepository.existsByName(category.getName())){
            throw new BadRequestException("Category with name=[%s] has already existed!".formatted(category.getName()));
        }
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.fromEntityToResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=[%s] not found!".formatted(id)));

        if (categoryRepository.existsByName(request.getName())){
            throw new BadRequestException("Category with name=[%s] has already existed!".formatted(request.getName()));
        }

        category.setName(request.getName());
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.fromEntityToResponse(savedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=[%s] not found!".formatted(id)));
        categoryRepository.delete(category);
    }

}
