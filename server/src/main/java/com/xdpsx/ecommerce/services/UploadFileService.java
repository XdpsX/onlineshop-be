package com.xdpsx.ecommerce.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadFileService {
    Map uploadFile(MultipartFile file, Map uploadOptions);
    boolean checkValidImgType(MultipartFile file);
    void deleteImage(String imgURL);
}
