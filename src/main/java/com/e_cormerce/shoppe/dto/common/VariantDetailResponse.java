package com.e_cormerce.shoppe.dto.common;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantDetailResponse {
    BigDecimal price;
    String thumbnail;
    double quantity;
    double discountPercentage;

    List<VariantValueDTO> variantValues;
}
