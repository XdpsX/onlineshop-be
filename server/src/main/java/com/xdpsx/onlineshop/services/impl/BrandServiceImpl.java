package com.xdpsx.onlineshop.services.impl;

import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.xdpsx.onlineshop.dtos.brand.BrandRequest;
import com.xdpsx.onlineshop.dtos.brand.BrandResponse;
import com.xdpsx.onlineshop.entities.Brand;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.exceptions.ResourceNotFoundException;
import com.xdpsx.onlineshop.mappers.BrandMapper;
import com.xdpsx.onlineshop.repositories.BrandRepository;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.services.BrandService;
import com.xdpsx.onlineshop.utils.CloudinaryUploader;
import com.xdpsx.onlineshop.utils.I18nUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static com.xdpsx.onlineshop.constants.FileConstants.*;

@Service
@RequiredArgsConstructor
public class BrandServiceImpl implements BrandService {
    private final CloudinaryUploader uploader;
    private final I18nUtils i18nUtils;
    private final BrandMapper brandMapper;
    private final BrandRepository brandRepository;
    private final CategoryRepository categoryRepository;

    private final static Map uploadOptions = ObjectUtils.asMap(
            "folder", BRAND_IMG_FOLDER,
            "transformation", new Transformation().width(BRAND_IMG_WIDTH).crop("scale")
    );

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public BrandResponse createBrand(BrandRequest request) {
        if (brandRepository.existsByName(request.getName())){
            throw new BadRequestException("Brand with name=%s already exists".formatted(request.getName()));
        }

        Brand brand = brandMapper.fromRequestToEntity(request);

        List<Category> categories = fetchCategories(request.getCategoryIds());
        brand.setCategories(categories);

        String publicId = uploader.uploadFile(request.getLogo(), uploadOptions);
        brand.setLogo(publicId);

        Brand savedBrand = brandRepository.save(brand);
        return brandMapper.fromEntityToResponse(savedBrand);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public BrandResponse updateBrand(Integer id, BrandRequest request) {
        Brand existingBrand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id=%s not found".formatted(id)));

        // Update name
        if (brandRepository.existsByName(request.getName())){
            throw new BadRequestException("Brand with name=%s already exists".formatted(request.getName()));
        }
        existingBrand.setName(request.getName());

        // Update categories
        if (request.getCategoryIds() != null){
            List<Category> newCategories = fetchCategories(request.getCategoryIds());
            existingBrand.setCategories(newCategories);
        }

        // Update logo
        if (request.getLogo() != null){
            String oldPublicId = existingBrand.getLogo();
            String newPublicId = uploader.uploadFile(request.getLogo(), uploadOptions);
            existingBrand.setLogo(newPublicId);
            uploader.deleteFile(oldPublicId);
        }

        Brand updatedBrand = brandRepository.save(existingBrand);
        return brandMapper.fromEntityToResponse(updatedBrand);
    }

    @Transactional(isolation = Isolation.READ_COMMITTED)
    @Override
    public void deleteBrand(Integer id) {
        Brand existingBrand = brandRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Brand with id=%s not found".formatted(id)));
        long countBrands = brandRepository.countBrandsInOtherTables(id);
        if (countBrands > 0){
            throw new BadRequestException(i18nUtils.getBrandCannotDeleteMsg(existingBrand.getName()));
        }
        brandRepository.delete(existingBrand);
        uploader.deleteFile(existingBrand.getLogo());
    }

    private List<Category> fetchCategories(Set<Integer> categoryIds) {
        return categoryIds.stream()
                .map(categoryId -> categoryRepository.findById(categoryId)
                        .orElseThrow(() -> new ResourceNotFoundException("Category with id=%s not found".formatted(categoryId))))
                .collect(Collectors.toList());
    }
}
