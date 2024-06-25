package com.xdpsx.ecommerce.validator;

import com.xdpsx.ecommerce.constants.AppConstants;
import com.xdpsx.ecommerce.exceptions.BadRequestException;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

public class ImageValidator implements ConstraintValidator<ImageConstraint, Object> {
    private int minWidth;
    private int maxNumber;

    @Override
    public void initialize(ImageConstraint constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        this.minWidth = constraintAnnotation.minWidth();
        this.maxNumber = constraintAnnotation.maxNumber();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        if (value instanceof List){
            List<MultipartFile> files = (List<MultipartFile>) value;
            if (files.size() > maxNumber){
                buildNewMessage(context, "Can not upload more than %s images".formatted(maxNumber));
                return false;
            }
            for (int i=0; i <= files.size()-1; i++) {
                MultipartFile curFile = files.get(i);
                if (!validateImageType(curFile)) {
                    buildNewMessage(context, "ERROR in Image %s. Only PNG or JPG images are supported".formatted(i));
                    return false;
                }

                if (!validateImageWidth(curFile)){
                    buildNewMessage(context, "ERROR in Image %s. Image width must be at least ".formatted(i) + minWidth + " pixels");
                    return false;
                }
            }
        }else if (value instanceof MultipartFile){
            MultipartFile file = (MultipartFile) value;
            if (!validateImageType(file)){
                buildNewMessage(context, "Only PNG or JPG images are supported");
                return false;
            }

            if (!validateImageWidth(file)){
                buildNewMessage(context, "Image width must be at least " + minWidth + " pixels");
                return false;
            }
        }
        return true;
    }

    private void buildNewMessage(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message).addConstraintViolation();
    }

    private boolean validateImageWidth(MultipartFile file) {
//        if (file == null || file.isEmpty()) {
//            return true; // Skip validation for empty files
//        }
        try {
            BufferedImage image = ImageIO.read(file.getInputStream());
            int imageWidth = image.getWidth();
            if (imageWidth < minWidth) {
                return false;
            }
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    private boolean validateImageType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !AppConstants.IMG_CONTENT_TYPES.contains(contentType)) {
            return false;
        }
        return true;
    }
}
