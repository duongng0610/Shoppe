package com.e_cormerce.shoppe.validation.auth.username;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = ValidUsernameValidator.class)
@Target({ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidUsername {
    String message() default
            "Invalid username provided.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
