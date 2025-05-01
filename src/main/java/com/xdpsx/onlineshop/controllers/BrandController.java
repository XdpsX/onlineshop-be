package com.xdpsx.onlineshop.controllers;

import java.util.Map;

import jakarta.validation.Valid;

import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.xdpsx.onlineshop.constants.messages.SMessage;
import com.xdpsx.onlineshop.dtos.brand.AdminBrandFilter;
import com.xdpsx.onlineshop.dtos.brand.AdminBrandResponse;
import com.xdpsx.onlineshop.dtos.brand.BrandRequest;
import com.xdpsx.onlineshop.dtos.brand.CreateBrandRequest;
import com.xdpsx.onlineshop.dtos.common.APIResponse;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.services.BrandService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/admin/brands")
    public APIResponse<PageResponse<AdminBrandResponse>> getAdminBrands(
            @ParameterObject @Valid AdminBrandFilter filter) {
        PageResponse<AdminBrandResponse> data = brandService.getAdminBrands(filter);
        return APIResponse.ok(data);
    }

    @PostMapping(path = "/brands/create")
    @ResponseStatus(HttpStatus.CREATED)
    public APIResponse<AdminBrandResponse> createBrand(@Valid @RequestBody CreateBrandRequest request) {
        AdminBrandResponse data = brandService.createBrand(request);
        return new APIResponse<>(HttpStatus.CREATED, data, SMessage.CREATE_SUCCESSFULLY);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<AdminBrandResponse> updateBrand(
            @PathVariable Integer id, @Valid @ModelAttribute BrandRequest request) {
        AdminBrandResponse response = brandService.updateBrand(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Map<String, Boolean>> checkExistsBrand(@RequestParam String name) {
        Map<String, Boolean> exists = brandService.checkExistsBrand(name);
        return ResponseEntity.ok(exists);
    }
}
