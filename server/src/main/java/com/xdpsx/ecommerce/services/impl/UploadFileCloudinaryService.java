package com.xdpsx.ecommerce.services.impl;

import com.cloudinary.Cloudinary;
import com.xdpsx.ecommerce.services.UploadFileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class UploadFileCloudinaryService implements UploadFileService {
    private final Cloudinary cloudinary;

    @Override
    public Map uploadFile(MultipartFile file, Map uploadOptions) {
        try{
            Map data = this.cloudinary.uploader().upload(file.getBytes(), uploadOptions);
            return data;
        }catch (IOException io){
            throw new RuntimeException("Uploading image is failed!");
        }
    }
}
