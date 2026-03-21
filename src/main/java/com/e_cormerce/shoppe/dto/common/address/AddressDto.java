package com.e_cormerce.shoppe.dto.common.address;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressDto {
    @NotBlank(message = "province is required")
    String province;
    @NotBlank(message = "district is required")
    String district;
    @NotBlank(message = "ward is required")
    String ward;
}
