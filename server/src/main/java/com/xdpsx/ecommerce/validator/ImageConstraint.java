package com.xdpsx.ecommerce.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = { ImageValidator.class })
public @interface ImageConstraint {
    String message() default "Invalid image";
    int minWidth();
    int maxNumber() default 1;
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
