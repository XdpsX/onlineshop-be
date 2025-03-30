package com.xdpsx.onlineshop.validations.validators;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import org.springframework.web.multipart.MultipartFile;

import com.xdpsx.onlineshop.validations.annotations.FileTypeConstraint;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FileTypeValidator implements ConstraintValidator<FileTypeConstraint, MultipartFile> {
    private String[] allowedTypes;

    @Override
    public void initialize(FileTypeConstraint constraintAnnotation) {
        this.allowedTypes = constraintAnnotation.allowedTypes();
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.getContentType() == null) {
            return false;
        }
        for (String type : allowedTypes) {
            if (type.equals(file.getContentType())) {
                try {
                    BufferedImage image = ImageIO.read(file.getInputStream());
                    return image != null;
                } catch (IOException e) {
                    log.error("Error while reading image from file", e);
                    return false;
                }
            }
        }

        return false;
    }

    //    private void buildNewMessage(ConstraintValidatorContext context, String message) {
    //        context.disableDefaultConstraintViolation();
    //        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    //    }

}
