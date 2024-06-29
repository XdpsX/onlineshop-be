package com.xdpsx.ecommerce.services;

import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.dtos.product.ProductPageParams;
import com.xdpsx.ecommerce.dtos.product.ProductRequest;
import com.xdpsx.ecommerce.dtos.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    PageResponse<ProductResponse> getAllProducts(ProductPageParams params);
    PageResponse<ProductResponse> getAllProducts(ProductPageParams params, Integer categoryId);
    PageResponse<ProductResponse> getAllProducts(ProductPageParams params, Boolean enabled, Integer categoryId);
    ProductResponse getProduct(Long id);
    ProductResponse createProduct(ProductRequest request, List<MultipartFile> files);
    ProductResponse updateProduct(Long id, ProductRequest request, List<MultipartFile> files);
    void deleteProduct(Long id);

    void updateProductEnabledStatus(Long id, boolean enabled);
}
