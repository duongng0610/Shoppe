package com.e_cormerce.shoppe.dto.common.address;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressCsv {
    int district_id;
    String district_name;
    int province_id;
    String province_name;
    String ward_id;
    String ward_name;
}
