package com.xdpsx.ecommerce.services;

import com.xdpsx.ecommerce.dtos.product.ProductRequest;
import com.xdpsx.ecommerce.dtos.product.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    List<ProductResponse> getAllProducts();
    ProductResponse getProduct(Long id);
    ProductResponse createProduct(ProductRequest request, List<MultipartFile> files);
    ProductResponse updateProduct(Long id, ProductRequest request, List<MultipartFile> files);
    void deleteProduct(Long id);
}
