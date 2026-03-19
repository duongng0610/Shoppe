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
    @NotBlank
    String province;
    @NotBlank
    String district;
    @NotBlank
    String ward;
}
