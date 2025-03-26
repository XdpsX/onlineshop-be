package com.xdpsx.onlineshop.dtos.product;

import com.xdpsx.onlineshop.dtos.common.AbstractPageParams;
import com.xdpsx.onlineshop.validations.SortConstraint;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import static com.xdpsx.onlineshop.constants.FieldConstants.*;

@EqualsAndHashCode(callSuper = true)
@Data @Builder
public class ProductParams extends AbstractPageParams {
    @SortConstraint(fields = {FIELD_NAME, FIELD_DATE, FIELD_PRICE})
    private String sort;

    @Min(value = 0)
    private Double minPrice;
    @Min(value = 0)
    private Double maxPrice;
    private Boolean hasPublished;
    private Boolean hasDiscount;
    private Boolean inStock;
    private Integer categoryId;
    private Integer brandId;
}
