package com.xdpsx.onlineshop.utils;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class CloudinaryUploader {
    private final Cloudinary cloudinary;

    public String uploadFile(MultipartFile file, Map uploadOptions) {
        try{
            Map response = this.cloudinary.uploader().upload(file.getBytes(), uploadOptions);
            return (String)response.get("public_id");
        }catch (IOException io){
            throw new RuntimeException("Uploading image is failed!");
        }
    }

    public void deleteFile(String publicId) {
        try{
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        }catch (IOException io){
            log.error("Deleting image is failed, public_id {}", publicId);
        }catch (Exception e){
            throw e;
        }
    }

    public String getFileUrl(String publicId) {
        return cloudinary.url()
                .publicId(publicId)
                .secure(true) // Sử dụng HTTPS
                .generate();
    }

}
