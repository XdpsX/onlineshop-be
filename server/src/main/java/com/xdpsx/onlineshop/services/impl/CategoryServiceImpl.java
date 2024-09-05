package com.xdpsx.onlineshop.services.impl;

import com.xdpsx.onlineshop.dtos.category.CategoryRequest;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.exceptions.ResourceNotFoundException;
import com.xdpsx.onlineshop.mappers.CategoryMapper;
import com.xdpsx.onlineshop.mappers.PageMapper;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.repositories.specs.BasicSpecification;
import com.xdpsx.onlineshop.services.CategoryService;
import com.xdpsx.onlineshop.utils.I18nUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final I18nUtils i18nUtils;
    private final CategoryMapper categoryMapper;
    private final PageMapper pageMapper;
    private final CategoryRepository categoryRepository;

    private final BasicSpecification<Category> spec;

    @Override
    public List<CategoryResponse> listAllCategories() {
        return categoryRepository
                .findAll(spec.getSortSpec("name"))
                .stream()
                .map(categoryMapper::fromEntityToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse createCategory(CategoryRequest request) {
        if (categoryRepository.existsByName(request.getName())){
            throw new BadRequestException("Category with name=%s already exists".formatted(request.getName()));
        }
        if (categoryRepository.existsBySlug(request.getSlug())){
            throw new BadRequestException("Category with slug=%s already exists".formatted(request.getSlug()));
        }
        Category category = categoryMapper.fromRequestToEntity(request);
        Category savedCategory = categoryRepository.save(category);
        return categoryMapper.fromEntityToResponse(savedCategory);
    }

    @Override
    public CategoryResponse updateCategory(Integer id, CategoryRequest request) {
        Category existingCat = categoryRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=%s not found".formatted(id)));

        // Update name
        if (!existingCat.getName().equals(request.getName())){
            if (categoryRepository.existsByName(request.getName())){
                throw new BadRequestException("Category with name=%s already exists".formatted(request.getName()));
            }
            existingCat.setName(request.getName());
        }

        // Update slug
        if (!existingCat.getSlug().equals(request.getSlug())){
            if (categoryRepository.existsBySlug(request.getSlug())){
                throw new BadRequestException("Category with slug=%s already exists".formatted(request.getSlug()));
            }
            existingCat.setSlug(request.getSlug());
        }

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

    @Override
    public PageResponse<CategoryResponse> listCategoriesByPage(PageParams params) {
        Page<Category> categoryPage = categoryRepository.findAll(
                spec.getFiltersSpec(params.getSearch(), params.getSort()),
                PageRequest.of(params.getPageNum() - 1, params.getPageSize())
        );
        return pageMapper.toCategoryPageResponse(categoryPage);
    }

    @Override
    public Map<String, Boolean> checkExistsCat(String name, String slug) {
        Map<String, Boolean> exists = new HashMap<>();
        exists.put("nameExists", categoryRepository.existsByName(name));
        exists.put("slugExists",categoryRepository.existsBySlug(slug));
        return exists;
    }
}
