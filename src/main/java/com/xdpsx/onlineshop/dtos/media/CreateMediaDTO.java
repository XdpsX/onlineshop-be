package com.xdpsx.onlineshop.dtos.media;

import static org.springframework.http.MediaType.*;

import jakarta.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.xdpsx.onlineshop.validations.annotations.FileTypeConstraint;

public record CreateMediaDTO(
        String caption,
        @NotNull
                @FileTypeConstraint(
                        allowedTypes = {IMAGE_JPEG_VALUE, IMAGE_PNG_VALUE, IMAGE_GIF_VALUE},
                        message = "File type not allowed. Allowed types are: JPEG, PNG, GIF")
                MultipartFile file) {}
