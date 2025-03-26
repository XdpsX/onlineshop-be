package com.xdpsx.onlineshop.dtos.product;

import com.xdpsx.onlineshop.validations.ImgConstraint;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.xdpsx.onlineshop.constants.FileConstants.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ProductCreateRequest {
    @NotBlank
    @Size(max = 255)
    private String name;

    @NotBlank
    @Size(max = 255)
    private String slug;

    @Min(value = 0)
    @Max(value = 1_000_000_000)
    private double price;

    @Min(value = 0)
    @Max(value = 100)
    private double discountPercent;

    private boolean inStock;

    private boolean published;

    @Size(max=4096)
    private String description;

    @NotNull
    private Integer categoryId;

    @NotNull
    private Integer brandId;

    @ImgConstraint(minWidth = PRODUCT_IMG_WIDTH, maxNumber = NUMBER_PRODUCT_IMAGES)
    private List<MultipartFile> images;
}
