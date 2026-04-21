package com.e_cormerce.shoppe.enums.product;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
public enum ProductStatus {
    BANNED("BANNED"),
    PENDING("PENDING"),
    ACTIVE("ACTIVE"),
    REJECTED("REJECTED"),
    HIDDEN("HIDDEN");

    String value;
}
