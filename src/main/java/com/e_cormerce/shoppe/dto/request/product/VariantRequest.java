package com.e_cormerce.shoppe.dto.request.product;

import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Data
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class VariantRequest {
    @Valid
    @NotEmpty(message = "variantValues is required")
    List<VariantAttributeDto> variantValues;

    @NotNull(message = "price of variant is required")
    @Positive(message = "price of variant must be greater than 0")
    BigDecimal price;


    @NotNull(message = "quantity of variant is required")
    @Positive(message = "quantity of variant must be greater than 0")
    Long quantity;
}
