package com.e_cormerce.shoppe.dto.request;

import com.e_cormerce.shoppe.entity.product.Variant;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class VariantRequest {
    List<VariantValueRequest> variantValues;
    BigDecimal price;
    int quantity;
    String url;
}
