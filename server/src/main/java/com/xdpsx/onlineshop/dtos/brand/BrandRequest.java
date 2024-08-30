package com.xdpsx.onlineshop.dtos.brand;

import com.xdpsx.onlineshop.validations.ImgConstraint;
import com.xdpsx.onlineshop.validations.OnCreate;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

import static com.xdpsx.onlineshop.constants.FileConstants.BRAND_IMG_WIDTH;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class BrandRequest {
    @NotBlank
    @Size(max = 64)
    private String name;

    @NotNull(groups = OnCreate.class)
    @ImgConstraint(minWidth = BRAND_IMG_WIDTH)
    private MultipartFile logo;

    @NotNull(groups = OnCreate.class)
    private Set<Integer> categoryIds;
}
