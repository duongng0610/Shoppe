package com.e_cormerce.shoppe.dto.response.address;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DistrictDto {
    int districtId;
    String districtName;
}

