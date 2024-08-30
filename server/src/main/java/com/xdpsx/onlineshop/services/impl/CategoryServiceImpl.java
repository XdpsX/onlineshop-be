package com.xdpsx.onlineshop.services.impl;

import com.xdpsx.onlineshop.dtos.request.CategoryRequest;
import com.xdpsx.onlineshop.dtos.response.CategoryResponse;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.exceptions.ResourceNotFoundException;
import com.xdpsx.onlineshop.mappers.CategoryMapper;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.services.CategoryService;
import com.xdpsx.onlineshop.utils.I18nUtils;
import com.xdpsx.onlineshop.utils.SlugConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final I18nUtils i18nUtils;
    private final CategoryMapper categoryMapper;
    private final CategoryRepository categoryRepository;

    @Override
    public List<CategoryResponse> listCategories() {
        return categoryRepository.findAll().stream()
                .map(categoryMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())){
            throw new BadRequestException("Category with name=%s already exists".formatted(request.getName()));
        }
        Category category = categoryMapper.fromRequestToEntity(request);
        category.setSlug(SlugConverter.toSlug(category.getName()));
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.fromEntityToResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        Category existingCat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=%s not found".formatted(id)));

        if (categoryRepository.existsByName(request.getName())){
            throw new BadRequestException("Category with name=%s already exists".formatted(request.getName()));
        }

        existingCat.setName(request.getName());
        existingCat.setSlug(SlugConverter.toSlug(request.getName()));
        Category savedCategory = categoryRepository.save(existingCat);
        return categoryMapper.fromEntityToResponse(savedCategory);
    }

    @Override
    public void deleteCategory(Integer id) {
        Category existingCat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=%s not found".formatted(id)));
        long countCategories = categoryRepository.countCategoriesInOtherTables(id);
        if (countCategories > 0){
            throw new BadRequestException(i18nUtils.getCatCannotDeleteMsg(existingCat.getName()));
        }
        categoryRepository.delete(existingCat);
    }
}
