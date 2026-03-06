package com.e_cormerce.shoppe.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CreateProductRequest {
    String name;
    String reason;
    BigDecimal originPrice;

    boolean hasVariant;
    int totalQuantity;

    Set<TypeRequest> types;
    Set<VariantRequest> variantRequests;
}
