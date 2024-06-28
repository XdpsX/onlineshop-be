package com.xdpsx.ecommerce.services.impl;

import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.dtos.product.ProductPageParams;
import com.xdpsx.ecommerce.dtos.product.ProductRequest;
import com.xdpsx.ecommerce.dtos.product.ProductResponse;
import com.xdpsx.ecommerce.entities.Category;
import com.xdpsx.ecommerce.entities.Product;
import com.xdpsx.ecommerce.entities.ProductImage;
import com.xdpsx.ecommerce.entities.Vendor;
import com.xdpsx.ecommerce.exceptions.ResourceNotFoundException;
import com.xdpsx.ecommerce.mappers.ProductMapper;
import com.xdpsx.ecommerce.repositories.CategoryRepository;
import com.xdpsx.ecommerce.repositories.ProductImageRepository;
import com.xdpsx.ecommerce.repositories.ProductRepository;
import com.xdpsx.ecommerce.repositories.VendorRepository;
import com.xdpsx.ecommerce.services.ProductService;
import com.xdpsx.ecommerce.services.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.xdpsx.ecommerce.constants.AppConstants.*;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductMapper productMapper;
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;
    private final VendorRepository vendorRepository;
    private final CategoryRepository categoryRepository;

    private final UploadFileService uploadFileService;

    @Override
    public PageResponse<ProductResponse> getAllProducts(ProductPageParams params, Boolean enabled) {
        Pageable pageable = PageRequest.of(params.getPageNum() - 1, params.getPageSize());
        Page<Product> productPage = productRepository.findWithFilters(
                pageable, params.getSearch(), params.getSort(), enabled, params.getMinPrice(), params.getMaxPrice(),
                params.isHasDiscount(), params.getVendorId(), params.getCategoryId()
        );
        List<ProductResponse> productResponses = productPage.getContent().stream()
                .map(productMapper::fromEntityToResponse)
                .collect(Collectors.toList());
        return PageResponse.<ProductResponse>builder()
                .items(productResponses)
                .pageNum(productPage.getNumber() + 1)
                .pageSize(productPage.getSize())
                .totalItems(productPage.getTotalElements())
                .totalPages(productPage.getTotalPages())
                .build();
    }

    @Override
    public ProductResponse getProduct(Long id) {
        Product product = productRepository.findByIdWithImages(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id=%s not found!".formatted(id)));
        return productMapper.fromEntityToResponse(product);
    }

    @Override
    public ProductResponse createProduct(ProductRequest request, List<MultipartFile> files) {
        Product product = productMapper.fromRequestToEntity(request);

        List<ProductImage> images = uploadProductImages(files, product);

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category with id=[%s] not found!".formatted(request.getCategoryId())));
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElseThrow(() -> new ResourceNotFoundException("Vendor with id=[%s] not found!".formatted(request.getVendorId())));

        product.setCategory(category);
        product.setVendor(vendor);
        product.setMainImage(images.get(0).getUrl());
        product.setImages(images);

        Product savedProduct = productRepository.save(product);
        return productMapper.fromEntityToResponse(savedProduct);
    }

    private List<ProductImage> uploadProductImages(List<MultipartFile> files, Product product){
        Map uploadOptions = ObjectUtils.asMap(
                "folder", PRODUCT_IMG_FOLDER,
                "transformation", new Transformation().width(PRODUCT_IMG_WIDTH).crop("scale")
        );
        List<ProductImage> images = new ArrayList<>();
        for (MultipartFile file: files){
            Map uploadFile = uploadFileService.uploadFile(file, uploadOptions);
            ProductImage productImage =  ProductImage.builder()
                    .url((String)uploadFile.get("url"))
                    .product(product)
                    .build();
            images.add(productImage);
        }
        return images;
    }

    @Transactional
    @Override
    public ProductResponse updateProduct(Long id, ProductRequest request, List<MultipartFile> files) {
        Product product = productRepository.findByIdWithImages(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id=%s not found!".formatted(id)));
        Product newProduct = productMapper.fromRequestToEntity(request);
        newProduct.setId(id);
        newProduct.setCategory(product.getCategory());
        newProduct.setVendor(product.getVendor());
        newProduct.setMainImage(product.getMainImage());
        newProduct.setImages(product.getImages());

        if (!product.getCategory().getId().equals(request.getCategoryId())){
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category with id=[%s] not found!".formatted(request.getCategoryId())));
            newProduct.setCategory(category);
        }
        if (!product.getVendor().getId().equals(request.getVendorId())){
            Vendor vendor = vendorRepository.findById(request.getVendorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Vendor with id=[%s] not found!".formatted(request.getVendorId())));

            newProduct.setVendor(vendor);
        }

        if (files != null && files.size() > 0){
            List<ProductImage> images = uploadProductImages(files, newProduct);

            newProduct.setMainImage(images.get(0).getUrl());
            newProduct.setImages(images);

            for (ProductImage productImage: product.getImages()) {
                uploadFileService.deleteImage(productImage.getUrl());
                productImageRepository.deleteById(productImage.getId());
            }
        }
        Product savedProduct = productRepository.save(newProduct);

        return productMapper.fromEntityToResponse(savedProduct);
    }

    @Override
    public void deleteProduct(Long id) {
        Product product = productRepository.findByIdWithImages(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id=%s not found!".formatted(id)));
        productRepository.delete(product);
        for (ProductImage productImage: product.getImages()) {
            uploadFileService.deleteImage(productImage.getUrl());
        }
    }

    public void enableProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id=%s not found!".formatted(productId)));

        product.setEnabled(true);
        productRepository.save(product);
    }

    public void disableProduct(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product with id=%s not found!".formatted(productId)));

        product.setEnabled(false);
        productRepository.save(product);
    }
}
