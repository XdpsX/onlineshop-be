package com.xdpsx.ecommerce.services.impl;

import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.xdpsx.ecommerce.constants.AppConstants;
import com.xdpsx.ecommerce.dtos.common.PageResponse;
import com.xdpsx.ecommerce.dtos.vendor.VendorPageRequest;
import com.xdpsx.ecommerce.dtos.vendor.VendorRequest;
import com.xdpsx.ecommerce.dtos.vendor.VendorResponse;
import com.xdpsx.ecommerce.entities.Vendor;
import com.xdpsx.ecommerce.exceptions.BadRequestException;
import com.xdpsx.ecommerce.exceptions.ResourceNotFoundException;
import com.xdpsx.ecommerce.mappers.VendorMapper;
import com.xdpsx.ecommerce.repositories.VendorRepository;
import com.xdpsx.ecommerce.services.UploadFileService;
import com.xdpsx.ecommerce.services.VendorService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorMapper vendorMapper;
    private final VendorRepository vendorRepository;
    private final UploadFileService uploadFileService;

    @Override
    public PageResponse<VendorResponse> getAllVendors(VendorPageRequest request) {
        Pageable pageable = PageRequest.of(request.getPageNum() - 1, request.getPageSize());
        Page<Vendor> vendorsPage = vendorRepository.findWithFilter(pageable, request.getSearch(), request.getSort());
        List<VendorResponse> vendorResponses = vendorsPage.getContent().stream()
                .map(vendorMapper::fromEntityToResponse)
                .collect(Collectors.toList());

        return PageResponse.<VendorResponse>builder()
                .items(vendorResponses)
                .pageNum(vendorsPage.getNumber() + 1)
                .pageSize(vendorsPage.getSize())
                .totalItems(vendorsPage.getTotalElements())
                .totalPages(vendorsPage.getTotalPages())
                .build();
    }

    @Override
    public VendorResponse getVendor(Integer id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor with id=[%s] not found!".formatted(id)));
        return vendorMapper.fromEntityToResponse(vendor);
    }

    @Override
    public VendorResponse createVendor(VendorRequest request, MultipartFile file) {
        Vendor vendor = vendorMapper.fromRequestToEntity(request);
        if (vendorRepository.existsByName(vendor.getName())){
            throw new BadRequestException("Vendor with name=[%s] has already existed!".formatted(vendor.getName()));
        }

        Map uploadOptions = ObjectUtils.asMap(
                "folder", AppConstants.VENDOR_IMG_FOLDER,
                "transformation", new Transformation().width(AppConstants.VENDOR_IMG_WIDTH).crop("scale")
        );
        Map uploadFile = uploadFileService.uploadFile(file, uploadOptions);
        vendor.setLogo((String)uploadFile.get("url"));

        Vendor savedVendor = vendorRepository.save(vendor);
        return vendorMapper.fromEntityToResponse(savedVendor);
    }

    @Override
    public VendorResponse updateVendor(Integer id, VendorRequest request, MultipartFile file) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor with id=[%s] not found!".formatted(id)));

        if (!vendor.getName().equals(request.getName())){
            if (vendorRepository.existsByName(request.getName())){
                throw new BadRequestException("Vendor with name=[%s] has already existed!".formatted(request.getName()));
            }
            vendor.setName(request.getName());
        }

        if (file != null) {
            String oldImageUrl = vendor.getLogo();

            Map uploadOptions = ObjectUtils.asMap(
                    "folder", AppConstants.VENDOR_IMG_FOLDER,
                    "transformation", new Transformation().width(AppConstants.VENDOR_IMG_WIDTH).crop("scale")
            );
            Map uploadFile = uploadFileService.uploadFile(file, uploadOptions);
            vendor.setLogo((String) uploadFile.get("url"));

            uploadFileService.deleteImage(oldImageUrl);
        }

        Vendor savedVendor = vendorRepository.save(vendor);
        return vendorMapper.fromEntityToResponse(savedVendor);
    }

    @Override
    public void deleteVendor(Integer id) {
        Vendor vendor = vendorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vendor with id=[%s] not found!".formatted(id)));
        vendorRepository.delete(vendor);
        uploadFileService.deleteImage(vendor.getLogo());
    }
}
