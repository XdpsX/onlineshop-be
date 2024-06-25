package com.xdpsx.ecommerce.services.impl;

import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.xdpsx.ecommerce.constants.AppConstants;
import com.xdpsx.ecommerce.dtos.product.ProductRequest;
import com.xdpsx.ecommerce.dtos.product.ProductResponse;
import com.xdpsx.ecommerce.entities.Category;
import com.xdpsx.ecommerce.entities.Product;
import com.xdpsx.ecommerce.entities.ProductImage;
import com.xdpsx.ecommerce.entities.Vendor;
import com.xdpsx.ecommerce.exceptions.BadRequestException;
import com.xdpsx.ecommerce.exceptions.ResourceNotFoundException;
import com.xdpsx.ecommerce.mappers.ProductMapper;
import com.xdpsx.ecommerce.repositories.CategoryRepository;
import com.xdpsx.ecommerce.repositories.ProductImageRepository;
import com.xdpsx.ecommerce.repositories.ProductRepository;
import com.xdpsx.ecommerce.repositories.VendorRepository;
import com.xdpsx.ecommerce.services.ProductService;
import com.xdpsx.ecommerce.services.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    public List<ProductResponse> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return products.stream()
                .map(productMapper::fromEntityToResponse)
                .collect(Collectors.toList());
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

        Map uploadOptions = ObjectUtils.asMap(
                "folder", AppConstants.PRODUCT_IMG_FOLDER,
                "transformation", new Transformation().width(AppConstants.PRODUCT_IMG_WIDTH).crop("scale")
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

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElse(null);
        Vendor vendor = vendorRepository.findById(request.getVendorId())
                .orElse(null);


        product.setCategory(category);
        product.setVendor(vendor);
        product.setMainImage(images.get(0).getUrl());
        product.setImages(images);

        Product savedProduct = productRepository.save(product);
        return productMapper.fromEntityToResponse(savedProduct);
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

        if (product.getCategory().getId() != request.getCategoryId()){
            Category category = null;
            if (request.getCategoryId() != null){
                category = categoryRepository.findById(request.getCategoryId())
                        .orElse(null);
            }
            newProduct.setCategory(category);
        }
        if (product.getVendor().getId() != request.getVendorId()){
            Vendor vendor = null;
            if (request.getVendorId() != null){
                vendor = vendorRepository.findById(request.getVendorId())
                        .orElse(null);
            }

            newProduct.setVendor(vendor);
        }

        if (files != null && files.size() > 0){
            Map uploadOptions = ObjectUtils.asMap(
                    "folder", AppConstants.PRODUCT_IMG_FOLDER,
                    "transformation", new Transformation().width(AppConstants.PRODUCT_IMG_WIDTH).crop("scale")
            );
            List<ProductImage> images = new ArrayList<>();
            for (MultipartFile file: files){
                Map uploadFile = uploadFileService.uploadFile(file, uploadOptions);
                ProductImage productImage =  ProductImage.builder()
                        .url((String)uploadFile.get("url"))
                        .product(newProduct)
                        .build();
                images.add(productImage);
            }

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
