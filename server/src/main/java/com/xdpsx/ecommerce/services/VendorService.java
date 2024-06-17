package com.xdpsx.ecommerce.services;

import com.xdpsx.ecommerce.dtos.category.CategoryRequest;
import com.xdpsx.ecommerce.dtos.vendor.VendorRequest;
import com.xdpsx.ecommerce.dtos.vendor.VendorResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface VendorService {
    List<VendorResponse> getAllVendors();
    VendorResponse getVendor(Integer id);
    VendorResponse createVendor(VendorRequest request, MultipartFile file);
    VendorResponse updateVendor(Integer id, VendorRequest request, MultipartFile file);
    void deleteVendor(Integer id);
}
