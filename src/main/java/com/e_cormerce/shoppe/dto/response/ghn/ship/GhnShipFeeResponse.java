package com.e_cormerce.shoppe.dto.response.ghn.ship;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnShipFeeResponse {
    Integer code;
    String message;
    GhnShipFeeDataResponse data;
}
