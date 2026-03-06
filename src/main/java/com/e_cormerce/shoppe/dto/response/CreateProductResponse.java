package com.e_cormerce.shoppe.dto.response;

import com.e_cormerce.shoppe.dto.request.VariantRequest;
import com.e_cormerce.shoppe.entity.product.Type;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.product.VariantValue;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class CreateProductResponse {
    String name;
    String reason;
    BigDecimal originPrice;

    LocalDateTime created_at;
    int total_quantity;

   List<Type> types;
    List<Variant> variants;
}
