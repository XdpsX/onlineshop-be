package com.xdpsx.onlineshop.services;

import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.dtos.product.*;

import java.util.Map;

public interface ProductService {
    PageResponse<ProductResponse> filterAllProducts(ProductParams params);
    ProductDetailsDTO getProductById(Long id);
    ProductResponse createProduct(ProductCreateRequest request);
    ProductResponse updateProduct(Long id, ProductUpdateRequest request);
    void deleteProduct(Long id);
    void publishProduct(Long id, boolean status);
    Map<String, Boolean> checkExistsProduct(String slug);
}
