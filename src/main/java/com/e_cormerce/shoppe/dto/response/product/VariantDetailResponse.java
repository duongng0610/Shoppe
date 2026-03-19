package com.e_cormerce.shoppe.dto.response.product;

import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VariantDetailResponse {
    String id;
    BigDecimal price;
    String thumbnail;
    double quantity;
    List<VariantAttributeDto> variantAttributes;
    long soldQuantity;
}
