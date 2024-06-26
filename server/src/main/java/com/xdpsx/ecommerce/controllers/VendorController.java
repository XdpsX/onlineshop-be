package com.xdpsx.ecommerce.controllers;

import com.xdpsx.ecommerce.constants.AppConstants;
import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.dtos.vendor.VendorPageRequest;
import com.xdpsx.ecommerce.dtos.vendor.VendorRequest;
import com.xdpsx.ecommerce.dtos.vendor.VendorResponse;
import com.xdpsx.ecommerce.services.VendorService;
import com.xdpsx.ecommerce.validator.ImageConstraint;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
@Validated
public class VendorController {
    private final VendorService vendorService;

    @GetMapping
    public ResponseEntity<PageResponse<VendorResponse>> getAllVendors(VendorPageRequest request) {
        PageResponse<VendorResponse> vendors = vendorService.getAllVendors(request);
        return ResponseEntity.ok(vendors);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VendorResponse> getVendor(@PathVariable Integer id) {
        VendorResponse vendor = vendorService.getVendor(id);
        return ResponseEntity.ok(vendor);
    }

    @PostMapping
    public ResponseEntity<VendorResponse> createVendor(
            @Valid @ModelAttribute VendorRequest request,
            @ImageConstraint(minWidth = AppConstants.VENDOR_IMG_WIDTH)
                @RequestParam MultipartFile logo
    ){
        VendorResponse createdVendor = vendorService.createVendor(request, logo);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(createdVendor.getId())
                .toUri();
        return ResponseEntity.created(uri).body(createdVendor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VendorResponse> updateVendor(
            @PathVariable Integer id,
            @Valid @ModelAttribute VendorRequest request,
            @ImageConstraint(minWidth = AppConstants.VENDOR_IMG_WIDTH)
                @RequestParam(required = false) MultipartFile logo
            ) {
        VendorResponse updatedVendor = vendorService.updateVendor(id, request, logo);
        return ResponseEntity.ok(updatedVendor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Integer id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }
}
