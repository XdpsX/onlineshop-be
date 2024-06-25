package com.xdpsx.ecommerce.services;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

public interface UploadFileService {
    Map uploadFile(MultipartFile file, Map uploadOptions);
    boolean checkValidImgType(MultipartFile file);
    boolean checkValidImgSize(MultipartFile file, int imgSize) throws IOException;
    void deleteImage(String imgURL);
}
