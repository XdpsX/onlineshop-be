package com.xdpsx.onlineshop.services.impl;

import static com.xdpsx.onlineshop.constants.FileConstants.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.xdpsx.onlineshop.dtos.brand.BrandNoCatsDTO;
import com.xdpsx.onlineshop.dtos.brand.BrandRequest;
import com.xdpsx.onlineshop.dtos.brand.BrandResponse;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.dtos.media.CloudinaryUploadResponse;
import com.xdpsx.onlineshop.entities.Brand;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.exceptions.DuplicateException;
import com.xdpsx.onlineshop.exceptions.ResourceNotFoundException;
import com.xdpsx.onlineshop.mappers.BrandMapper;
import com.xdpsx.onlineshop.mappers.PageMapper;
import com.xdpsx.onlineshop.repositories.BrandRepository;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.repositories.specs.BasicSpecification;
import com.xdpsx.onlineshop.services.BrandService;
import com.xdpsx.onlineshop.utils.CloudinaryUploader;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final CloudinaryUploader uploader;
    private final BrandMapper brandMapper;
    private final PageMapper pageMapper;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;
    private final BasicSpecification<Brand> spec;

    private static final Map uploadOptions = ObjectUtils.asMap(
            "folder",
            BRAND_IMG_FOLDER,
            "transformation",
            new Transformation().width(BRAND_IMG_WIDTH).crop("scale"));

    @Override
    public PageResponse<BrandResponse> listBrandsByPage(PageParams params) {
        Page<Brand> brandPage = brandRepository.findAll(
                spec.getFiltersSpec(params.getSearch(), params.getSort()),
                PageRequest.of(params.getPageNum() - 1, params.getPageSize()));
        return pageMapper.toBrandPageResponse(brandPage);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public BrandResponse createBrand(BrandRequest request) {
        if (brandRepository.existsByName(request.getName())) {
            throw new DuplicateException("Brand with name=%s already exists".formatted(request.getName()));
        }

        Brand brand = brandMapper.fromRequestToEntity(request);

        List<Category> categories = fetchCategories(request.getCategoryIds());
        brand.setCategories(categories);

        CloudinaryUploadResponse response = uploader.uploadFile(request.getLogo(), uploadOptions);
        brand.setLogo(response.url());

        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.fromEntityToResponse(savedBrand);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public BrandResponse updateBrand(Integer id, BrandRequest request) {
        Brand existingBrand = brandRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id=%s not found".formatted(id)));

        // Update name
        if (!existingBrand.getName().equals(request.getName())) {
            if (brandRepository.existsByName(request.getName())) {
                throw new DuplicateException("Brand with name=%s already exists".formatted(request.getName()));
            }
            existingBrand.setName(request.getName());
        }

        // Update categories
        if (request.getCategoryIds() != null) {
            List<Category> newCategories = fetchCategories(request.getCategoryIds());
            existingBrand.setCategories(newCategories);
        }

        // Update logo
        if (request.getLogo() != null) {
            String oldPublicId = existingBrand.getLogo();
            CloudinaryUploadResponse response = uploader.uploadFile(request.getLogo(), uploadOptions);
            existingBrand.setLogo(response.url());
            uploader.deleteFile(oldPublicId);
        }

        Brand updatedBrand = brandRepository.save(existingBrand);
        return brandMapper.fromEntityToResponse(updatedBrand);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void deleteBrand(Integer id) {
        Brand existingBrand = brandRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id=%s not found".formatted(id)));
        //        long countBrands = brandRepository.countBrandsInOtherTables(id);
        //        if (countBrands > 0){
        //            throw new BadRequestException(i18nUtils.getBrandCannotDeleteMsg(existingBrand.getName()));
        //        }
        brandRepository.delete(existingBrand);
        uploader.deleteFile(existingBrand.getLogo());
    }

    @Override
    public Map<String, Boolean> checkExistsBrand(String name) {
        Map<String, Boolean> exists = new HashMap<>();
        exists.put("nameExists", brandRepository.existsByName(name));
        return exists;
    }

    //    @Transactional(readOnly = true)
    @Override
    public List<BrandNoCatsDTO> listBrandsByCategoryId(Integer categoryId) {
        Category category = categoryRepository
                .findById(categoryId)
                .orElseThrow(
                        () -> new ResourceNotFoundException("Category with id=%s not found".formatted(categoryId)));
        //        List<Brand> brands = category.getBrands();
        List<Brand> brands = brandRepository.findBrandsByCategoryId(category.getId());
        return brands.stream().map(brandMapper::fromEntityToNotCatsDTO).collect(Collectors.toList());
    }

    private List<Category> fetchCategories(Set<Integer> categoryIds) {
        return categoryIds.stream()
                .map(categoryId -> categoryRepository
                        .findById(categoryId)
                        .orElseThrow(() ->
                                new ResourceNotFoundException("Category with id=%s not found".formatted(categoryId))))
                .collect(Collectors.toList());
    }
}
