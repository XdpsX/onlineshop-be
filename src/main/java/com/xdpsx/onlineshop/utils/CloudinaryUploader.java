package com.xdpsx.onlineshop.utils;

import java.io.IOException;
import java.util.Map;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xdpsx.onlineshop.dtos.media.CloudinaryUploadResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CloudinaryUploader {
    private final Cloudinary cloudinary;
    private final ObjectMapper objectMapper;

    public CloudinaryUploadResponse uploadFile(MultipartFile file, Map uploadOptions) {
        try {
            Map response = cloudinary.uploader().upload(file.getBytes(), uploadOptions);
            return objectMapper.convertValue(response, CloudinaryUploadResponse.class);
        } catch (IOException io) {
            throw new RuntimeException("Uploading image failed!", io);
        }
    }

    public void deleteFile(String publicId) {
        try {
            cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
        } catch (IOException io) {
            log.error("Deleting image is failed, public_id {}", publicId);
        } catch (Exception e) {
            throw e;
        }
    }

    public String getFileUrl(String publicId) {
        return cloudinary
                .url()
                .publicId(publicId)
                .secure(true) // Sử dụng HTTPS
                .generate();
    }
}
