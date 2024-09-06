package com.xdpsx.onlineshop.apis;

import com.xdpsx.onlineshop.dtos.brand.BrandRequest;
import com.xdpsx.onlineshop.dtos.brand.BrandResponse;
import com.xdpsx.onlineshop.dtos.common.PageParams;
import com.xdpsx.onlineshop.dtos.common.PageResponse;
import com.xdpsx.onlineshop.services.BrandService;
import com.xdpsx.onlineshop.validations.OnCreate;
import jakarta.validation.Valid;
import jakarta.validation.groups.Default;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandController {
    private final BrandService brandService;

    @GetMapping("/filters")
    public ResponseEntity<PageResponse<BrandResponse>> getBrandsByPage(@Valid PageParams params) {
        PageResponse<BrandResponse> pageResponse = brandService.listBrandsByPage(params);
        return ResponseEntity.status(HttpStatus.OK).body(pageResponse);
    }

    @PostMapping(path="/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<BrandResponse> createBrand(
            @Validated({OnCreate.class, Default.class}) @ModelAttribute BrandRequest request) {
        BrandResponse response = brandService.createBrand(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}/update")
    public ResponseEntity<BrandResponse> updateBrand(@PathVariable Integer id, @Valid @ModelAttribute BrandRequest request) {
        BrandResponse response = brandService.updateBrand(id, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}/delete")
    public ResponseEntity<Void> deleteBrand(@PathVariable Integer id) {
        brandService.deleteBrand(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists")
    public ResponseEntity<Map<String, Boolean>> checkExistsBrand(
            @RequestParam String name
    ){
        Map<String, Boolean> exists = brandService.checkExistsBrand(name);
        return ResponseEntity.ok(exists);
    }
}
