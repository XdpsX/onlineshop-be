package com.xdpsx.onlineshop.services.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.xdpsx.onlineshop.constants.messages.EMessage;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.dtos.category.CreateCategoryDTO;
import com.xdpsx.onlineshop.dtos.category.UpdateCategoryDTO;
import com.xdpsx.onlineshop.dtos.common.ModifyExclusiveDTO;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.entities.Media;
import com.xdpsx.onlineshop.entities.enums.MediaResourceType;
import com.xdpsx.onlineshop.exceptions.*;
import com.xdpsx.onlineshop.mappers.CategoryMapper;
import com.xdpsx.onlineshop.mappers.PageMapper;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.repositories.MediaRepository;
import com.xdpsx.onlineshop.repositories.specs.BasicSpecification;
import com.xdpsx.onlineshop.services.CategoryService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final PageMapper pageMapper;
    private final CategoryRepository categoryRepository;
    private final MediaRepository mediaRepository;

    private final BasicSpecification<Category> spec;

    @Override
    public List<CategoryResponse> getAllCategories() {
        return categoryRepository.findAll(spec.getSortSpec("name")).stream()
                .map(CategoryMapper.INSTANCE::toResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CategoryResponse createCategory(CreateCategoryDTO request) {
        Category category = CategoryMapper.INSTANCE.toEntity(request);

        if (categoryRepository.existsByName(request.name())) {
            throw new DuplicateException(EMessage.DATA_EXISTS, request.name());
        }

        if (request.parentId() != null) {
            Category parent = categoryRepository
                    .findById(request.parentId())
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, request.parentId()));
            category.setParent(parent);
        }

        if (request.imageId() != null) {
            Media image = mediaRepository
                    .findById(request.imageId())
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, request.imageId()));
            if (!image.getResourceType().equals(MediaResourceType.CATEGORY)) {
                throw new InvalidResourceTypeException(EMessage.INVALID_RESOURCE_TYPE);
            }
            image.setTempFlg(false);
            category.setImage(image);
        }

        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toResponse(savedCategory);
    }

    @Override
    @Transactional
    public CategoryResponse updateCategory(Integer id, UpdateCategoryDTO request) {
        Category category = categoryRepository
                .findByIdWithParent(id)
                .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, id));

        if (category.getUpdatedAt() != null && !request.lastRetrievedAt().isAfter(category.getUpdatedAt())) {
            throw new ModifyExclusiveException(EMessage.MODIFY_EXCLUSIVE);
        }

        // Update name
        if (!category.getName().equals(request.name())) {
            if (categoryRepository.existsByName(request.name())) {
                throw new DuplicateException(EMessage.DATA_EXISTS, request.name());
            }
            category.setName(request.name());
        }

        category.setPublicFlg(request.publicFlg());
        // Update image
        updateCategoryImage(category, request.imageId());

        // Update parent
        if (!isSameParent(category, request.parentId())) {
            category.setParent(getParentCategory(request.parentId()));
        }

        Category savedCategory = categoryRepository.save(category);
        return CategoryMapper.INSTANCE.toResponse(savedCategory);
    }

    private void updateCategoryImage(Category category, String newImageId) {
        Media oldImage = category.getImage();

        if (oldImage == null && newImageId == null) return;

        // If have old image:
        // 1. No new image (newImageId == null) => Delete old image
        // 2. New image != old image => Delete old image
        if (oldImage != null && !oldImage.getId().equals(newImageId)) {
            oldImage.setDeleteFlg(true);
            mediaRepository.save(oldImage);
            category.setImage(null);
        }

        // if have new image and new image != old image => Update image
        if (newImageId != null && (oldImage == null || !oldImage.getId().equals(newImageId))) {
            Media newImage = mediaRepository
                    .findById(newImageId)
                    .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, newImageId));
            if (!newImage.getResourceType().equals(MediaResourceType.CATEGORY)) {
                throw new InvalidResourceTypeException(EMessage.INVALID_RESOURCE_TYPE);
            }

            newImage.setTempFlg(false);
            mediaRepository.save(newImage);

            category.setImage(newImage);
        }
    }

    private boolean isSameParent(Category category, Integer newParentId) {
        return (category.getParent() == null && newParentId == null)
                || (category.getParent() != null && category.getParent().getId().equals(newParentId));
    }

    private Category getParentCategory(Integer parentId) {
        return (parentId == null)
                ? null
                : categoryRepository
                        .findById(parentId)
                        .orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, parentId));
    }

    @Override
    @Transactional
    public void deleteCategory(Integer id, ModifyExclusiveDTO request) {
        Category category =
                categoryRepository.findById(id).orElseThrow(() -> new NotFoundException(EMessage.NOT_FOUND, id));
        if (!request.lastRetrievedAt().isAfter(category.getUpdatedAt())) {
            throw new ModifyExclusiveException(EMessage.MODIFY_EXCLUSIVE);
        }
        long countCategories = categoryRepository.countCategoriesInOtherTables(id);
        if (countCategories > 0) {
            throw new InUseException(EMessage.IN_USE);
        }
        if (category.getImage() != null) {
            Media image = category.getImage();
            image.setDeleteFlg(true);
            mediaRepository.save(image);
        }
        categoryRepository.delete(category);
    }

    @Override
    public PageResponse<CategoryResponse> getCategoriesPage(PageParams params) {
        Page<Category> categoryPage = categoryRepository.findAll(
                spec.getFiltersSpec(params.getSearch(), params.getSort()),
                PageRequest.of(params.getPageNum() - 1, params.getPageSize()));
        return pageMapper.toCategoryPageResponse(categoryPage);
    }

    @Override
    public Map<String, Boolean> checkExistsCat(String name, String slug) {
        Map<String, Boolean> exists = new HashMap<>();
        exists.put("nameExists", categoryRepository.existsByName(name));
        return exists;
    }
}
