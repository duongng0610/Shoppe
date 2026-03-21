package com.e_cormerce.shoppe.dto.common.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Valid
public class VariantAttributeDto {
    @NotBlank(message = "name of variant attribute is required")
    String name;

    @NotBlank(message = "value of variant attribute is required")
    String value;
}
