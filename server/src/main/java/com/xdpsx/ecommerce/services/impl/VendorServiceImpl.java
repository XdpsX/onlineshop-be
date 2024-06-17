package com.xdpsx.ecommerce.services.impl;

import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VendorServiceImpl implements VendorService {
    private final VendorMapper vendorMapper;
    private final VendorRepository vendorRepository;
    private final UploadFileService uploadFileService;

    private final static int IMG_SIZE = 320;
    private final static String IMG_FOLDER = "vendors";

    @Override
    public List<VendorResponse> getAllVendors() {
        List<Vendor> vendors = vendorRepository.findAll();
        return vendors.stream()
                .map(vendorMapper::fromEntityToResponse)
                .collect(Collectors.toList());
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

        if (!uploadFileService.checkValidImgType(file)){
            throw new BadRequestException("Only PNG or JPG images are supported");
        }

        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            int imageWidth = image.getWidth();
            if (imageWidth < IMG_SIZE) {
                throw new BadRequestException("Image width must be at least " + IMG_SIZE + " pixels");
            }

            Map uploadOptions = ObjectUtils.asMap(
                    "folder", IMG_FOLDER,
                    "transformation", new Transformation().width(IMG_SIZE).crop("scale")
            );
            Map uploadFile = uploadFileService.uploadFile(file, uploadOptions);
            vendor.setLogo((String)uploadFile.get("url"));

            Vendor savedVendor = vendorRepository.save(vendor);
            return vendorMapper.fromEntityToResponse(savedVendor);
        } catch (IOException e) {
            throw new BadRequestException("Error reading image file.");
        }
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
            if (!uploadFileService.checkValidImgType(file)) {
                throw new BadRequestException("Only PNG or JPG images are supported");
            }

            String oldImageUrl = vendor.getLogo();

            try {
                BufferedImage image = ImageIO.read(file.getInputStream());
                int imageWidth = image.getWidth();
                if (imageWidth < IMG_SIZE) {
                    throw new BadRequestException("Image width must be at least " + IMG_SIZE + " pixels");
                }

                Map uploadOptions = ObjectUtils.asMap(
                        "folder", IMG_FOLDER,
                        "transformation", new Transformation().width(IMG_SIZE).crop("scale")
                );
                Map uploadFile = uploadFileService.uploadFile(file, uploadOptions);
                vendor.setLogo((String) uploadFile.get("url"));

                uploadFileService.deleteImage(oldImageUrl);
            } catch (IOException e) {
                throw new BadRequestException("Error reading image file.");
            }
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
