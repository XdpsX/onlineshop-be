package com.xdpsx.ecommerce.services;

import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.dtos.vendor.VendorPageRequest;
import com.xdpsx.ecommerce.dtos.vendor.VendorRequest;
import com.xdpsx.ecommerce.dtos.vendor.VendorResponse;
import org.springframework.web.multipart.MultipartFile;

public interface VendorService {
    PageResponse<VendorResponse> getAllVendors(VendorPageRequest request);
    VendorResponse getVendor(Integer id);
    VendorResponse createVendor(VendorRequest request, MultipartFile file);
    VendorResponse updateVendor(Integer id, VendorRequest request, MultipartFile file);
    void deleteVendor(Integer id);
}
