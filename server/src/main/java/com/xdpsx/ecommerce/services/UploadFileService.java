package com.xdpsx.ecommerce.services;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

public interface UploadFileService {
    Map uploadFile(MultipartFile file, Map uploadOptions);
}
