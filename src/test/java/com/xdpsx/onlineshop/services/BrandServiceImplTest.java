package com.xdpsx.onlineshop.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import com.xdpsx.onlineshop.dtos.brand.BrandRequest;
import com.xdpsx.onlineshop.dtos.brand.BrandResponse;
import com.xdpsx.onlineshop.dtos.category.CategoryResponse;
import com.xdpsx.onlineshop.entities.Brand;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.exceptions.BadRequestException;
import com.xdpsx.onlineshop.exceptions.ResourceNotFoundException;
import com.xdpsx.onlineshop.mappers.BrandMapper;
import com.xdpsx.onlineshop.repositories.BrandRepository;
import com.xdpsx.onlineshop.repositories.CategoryRepository;
import com.xdpsx.onlineshop.services.impl.BrandServiceImpl;
import com.xdpsx.onlineshop.utils.CloudinaryUploader;

@ExtendWith(MockitoExtension.class)
class BrandServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BrandRepository brandRepository;

    @Mock
    private CloudinaryUploader uploader;

    @Mock
    private BrandMapper brandMapper;

    @Mock
    private MultipartFile logo;

    @InjectMocks
    private BrandServiceImpl brandService;

    private Category category1;
    private CategoryResponse category1Response;
    private Category category2;
    private CategoryResponse category2Response;

    @BeforeEach
    void setUp() {
        category1 = Category.builder().id(1).name("Category 1").build();
        category2 = Category.builder().id(2).name("Category 2").build();
        category1Response = CategoryResponse.builder().id(1).name("Category 1").build();
        category2Response = CategoryResponse.builder().id(2).name("Category 2").build();
    }

    @DisplayName("Create brand successfully")
    @Test
    void testCreateBrand_ShouldSaveBrand() {
        BrandRequest brandRequest = BrandRequest.builder()
                .name("Brand 1")
                .logo(logo)
                .categoryIds(Set.of(1))
                .build();
        Brand brand = Brand.builder().id(1).name("Brand 1").build();
        Brand savedBrand = Brand.builder()
                .id(1)
                .name("Brand 1")
                .logo("brand-1.jpg")
                .categories(List.of(category1))
                .build();
        BrandResponse brandResponse = BrandResponse.builder()
                .id(1)
                .name("Brand 1")
                .logo("brand-1.jpg")
                .categories(List.of(category1Response))
                .build();

        when(brandRepository.existsByName(brandRequest.getName())).thenReturn(false);
        when(brandMapper.fromRequestToEntity(brandRequest)).thenReturn(brand);
        when(uploader.uploadFile(any(MultipartFile.class), anyMap())).thenReturn("brand-1.jpg");
        when(categoryRepository.findById(category1.getId())).thenReturn(Optional.of(category1));
        when(brandRepository.save(brand)).thenReturn(savedBrand);
        when(brandMapper.fromEntityToResponse(savedBrand)).thenReturn(brandResponse);

        BrandResponse response = brandService.createBrand(brandRequest);

        assertEquals(brandResponse.getName(), response.getName());
        assertEquals(brandResponse.getLogo(), response.getLogo());
        verify(uploader).uploadFile(any(MultipartFile.class), anyMap());
        verify(brandRepository).save(brand);
    }

    @DisplayName("Create brand with existing name")
    //    @Test
    void testCreateBrand_WhenBrandAlreadyExists_ShouldThrowBadRequestException() {
        BrandRequest brandRequest = BrandRequest.builder()
                .name("Brand 1")
                .logo(logo)
                .categoryIds(Set.of(1))
                .build();

        when(brandRepository.existsByName(brandRequest.getName())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> brandService.createBrand(brandRequest));

        verify(uploader, never()).uploadFile(any(MultipartFile.class), anyMap());
        verify(brandRepository, never()).save(any());
    }

    @DisplayName("Create brand when not found category")
    @Test
    void testCreateBrand_WhenNotFoundCategory_ShouldThrowResourceNotFoundException() {
        BrandRequest brandRequest = BrandRequest.builder()
                .name("Brand 1")
                .logo(logo)
                .categoryIds(Set.of(1))
                .build();
        Brand brand = Brand.builder().id(1).name("Brand 1").build();

        when(brandRepository.existsByName(brandRequest.getName())).thenReturn(false);
        when(brandMapper.fromRequestToEntity(brandRequest)).thenReturn(brand);
        when(categoryRepository.findById(category1.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> brandService.createBrand(brandRequest));

        verify(uploader, never()).uploadFile(any(MultipartFile.class), anyMap());
        verify(brandRepository, never()).save(any());
    }

    @DisplayName("Update brand with new image and categories successfully")
    @Test
    void testUpdateBrand_WhenProvideNewImageAndCategories_ShouldUpdateBrand() {
        MultipartFile newLogo = mock(MultipartFile.class);
        Integer brandId = 1;
        BrandRequest brandRequest = BrandRequest.builder()
                .name("Brand 1 Updated")
                .logo(newLogo)
                .categoryIds(Set.of(category2.getId()))
                .build();
        Brand existingBrand = Brand.builder()
                .id(brandId)
                .name("Brand 1")
                .logo("brand-1.jpg")
                .categories(List.of(category1))
                .build();
        Brand updatedBrand = Brand.builder()
                .id(brandId)
                .name("Brand 1 Updated")
                .logo("brand-1-updated.jpg")
                .categories(List.of(category2))
                .build();
        BrandResponse brandResponse = BrandResponse.builder()
                .id(brandId)
                .name("Brand 1 Updated")
                .logo("brand-1-updated.jpg")
                .categories(List.of(category2Response))
                .build();

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.existsByName(brandRequest.getName())).thenReturn(false);
        when(categoryRepository.findById(category2.getId())).thenReturn(Optional.of(category2));
        when(uploader.uploadFile(any(MultipartFile.class), anyMap())).thenReturn("brand-1-updated.jpg");
        when(brandRepository.save(existingBrand)).thenReturn(updatedBrand);
        when(brandMapper.fromEntityToResponse(updatedBrand)).thenReturn(brandResponse);

        BrandResponse response = brandService.updateBrand(brandId, brandRequest);

        assertEquals(brandResponse.getName(), response.getName());
        assertEquals(brandResponse.getLogo(), response.getLogo());
        verify(uploader).uploadFile(any(MultipartFile.class), anyMap());
        verify(brandRepository).save(existingBrand);
    }

    @DisplayName("Update brand without new image and categories successfully")
    @Test
    void testUpdateBrand_WhenNotProvideNewImageAndCategories_ShouldUpdateBrand() {
        Integer brandId = 1;
        BrandRequest brandRequest =
                BrandRequest.builder().name("Brand 1 Updated").build();
        Brand existingBrand = Brand.builder()
                .id(brandId)
                .name("Brand 1")
                .logo("brand-1.jpg")
                .categories(List.of(category1))
                .build();
        Brand updatedBrand = Brand.builder()
                .id(brandId)
                .name("Brand 1 Updated")
                .logo("brand-1.jpg")
                .categories(List.of(category1))
                .build();
        BrandResponse brandResponse = BrandResponse.builder()
                .id(brandId)
                .name("Brand 1 Updated")
                .logo("brand-1.jpg")
                .categories(List.of(category1Response))
                .build();

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.existsByName(brandRequest.getName())).thenReturn(false);
        when(brandRepository.save(existingBrand)).thenReturn(updatedBrand);
        when(brandMapper.fromEntityToResponse(updatedBrand)).thenReturn(brandResponse);

        BrandResponse response = brandService.updateBrand(brandId, brandRequest);

        assertEquals(brandResponse.getName(), response.getName());
        assertEquals(brandResponse.getLogo(), response.getLogo());
        verify(uploader, never()).uploadFile(any(MultipartFile.class), anyMap());
        verify(categoryRepository, never()).findById(any());
        verify(brandRepository).save(existingBrand);
    }

    @DisplayName("Update not found brand")
    @Test
    void testUpdateBrand_WhenBrandNotFound_ShouldThrowResourceNotFoundException() {
        Integer brandId = 1;
        BrandRequest brandRequest =
                BrandRequest.builder().name("Brand 1 Updated").build();

        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> brandService.updateBrand(brandId, brandRequest));

        verify(uploader, never()).uploadFile(any(MultipartFile.class), anyMap());
        verify(brandRepository, never()).save(any());
    }

    @DisplayName("Update brand with existing name")
    //    @Test
    void testUpdateBrand_WhenBrandAlreadyExists_ShouldThrowBadRequestException() {
        Integer brandId = 1;
        BrandRequest brandRequest =
                BrandRequest.builder().name("Brand 1 Updated").build();
        Brand existingBrand = Brand.builder()
                .id(brandId)
                .name("Brand 1")
                .logo("brand-1.jpg")
                .categories(List.of(category1))
                .build();

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.existsByName(brandRequest.getName())).thenReturn(true);

        assertThrows(BadRequestException.class, () -> brandService.updateBrand(brandId, brandRequest));

        verify(uploader, never()).uploadFile(any(MultipartFile.class), anyMap());
        verify(brandRepository, never()).save(any());
    }

    @DisplayName("Update brand when not found new categories")
    @Test
    void testUpdateBrand_WhenNotFoundNewCategories_ShouldThrowResourceNotFoundException() {
        MultipartFile newLogo = mock(MultipartFile.class);
        Integer brandId = 1;
        BrandRequest brandRequest = BrandRequest.builder()
                .name("Brand 1 Updated")
                .logo(newLogo)
                .categoryIds(Set.of(category2.getId()))
                .build();
        Brand existingBrand = Brand.builder()
                .id(brandId)
                .name("Brand 1")
                .logo("brand-1.jpg")
                .categories(List.of(category1))
                .build();

        when(brandRepository.findById(brandId)).thenReturn(Optional.of(existingBrand));
        when(brandRepository.existsByName(brandRequest.getName())).thenReturn(false);
        when(categoryRepository.findById(category2.getId())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> brandService.updateBrand(brandId, brandRequest));

        verify(uploader, never()).uploadFile(any(MultipartFile.class), anyMap());
        verify(brandRepository, never()).save(any());
    }

    @DisplayName("Delete brand successfully")
    @Test
    void testDeleteBrand_ShouldDeleteBrand() {
        Integer brandId = 1;
        Brand brand =
                Brand.builder().id(brandId).name("Brand 1").logo("brand-1.jpg").build();
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));
        //        when(brandRepository.countBrandsInOtherTables(brandId)).thenReturn(0L);

        brandService.deleteBrand(brandId);

        verify(brandRepository).delete(brand);
        verify(uploader).deleteFile("brand-1.jpg");
    }

    @DisplayName("Delete brand when not found brand")
    @Test
    void testDeleteBrand_WhenNotFoundBrand_ShouldThrowResourceNotFoundException() {
        Integer brandId = 1;
        when(brandRepository.findById(brandId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> brandService.deleteBrand(brandId));
        //        verify(brandRepository, never()).delete(any());
        verify(uploader, never()).deleteFile(anyString());
    }

    @DisplayName("Delete brand that is currently in use")
    //    @Test
    void testDeleteBrand_WhenBrandInUse_ShouldThrowBadRequestException() {
        Integer brandId = 1;
        Brand brand =
                Brand.builder().id(brandId).name("Brand 1").logo("brand-1.jpg").build();
        when(brandRepository.findById(brandId)).thenReturn(Optional.of(brand));
        //        when(brandRepository.countBrandsInOtherTables(brandId)).thenReturn(1L);

        BadRequestException exception = assertThrows(BadRequestException.class, () -> brandService.deleteBrand(1));

        assertEquals(exception.getMessage(), "Cannot delete brand in use");
        //        verify(brandRepository, never()).delete(any());
        verify(uploader, never()).deleteFile(anyString());
    }
}
