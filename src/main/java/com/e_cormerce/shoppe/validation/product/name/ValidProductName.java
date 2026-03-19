package com.e_cormerce.shoppe.validation.product.name;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ProductNameValidator.class)
@Target(value = ElementType.FIELD)
@Retention(value = RetentionPolicy.RUNTIME)
public @interface ValidProductName {
    String message() default "Invalid product name";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
