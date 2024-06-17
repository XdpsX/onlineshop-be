package com.xdpsx.ecommerce.controllers;

import com.xdpsx.ecommerce.dtos.vendor.VendorRequest;
import com.xdpsx.ecommerce.dtos.vendor.VendorResponse;
import com.xdpsx.ecommerce.services.VendorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/vendors")
@RequiredArgsConstructor
public class VendorController {
    private final VendorService vendorService;

    @GetMapping
    public ResponseEntity<List<VendorResponse>> getAllVendors() {
        List<VendorResponse> vendors = vendorService.getAllVendors();
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
            @RequestParam("logo") MultipartFile multipartFile
    ){
        VendorResponse createdVendor = vendorService.createVendor(request, multipartFile);
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
            @RequestParam(value = "logo", required = false) MultipartFile multipartFile
            ) {
        VendorResponse updatedVendor = vendorService.updateVendor(id, request, multipartFile);
        return ResponseEntity.ok(updatedVendor);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVendor(@PathVariable Integer id) {
        vendorService.deleteVendor(id);
        return ResponseEntity.noContent().build();
    }
}
