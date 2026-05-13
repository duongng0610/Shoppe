package com.e_cormerce.shoppe.dto.response.ghn.order_ship.info;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GhnOrderShipInfoResponse {
  Integer code;
  String message;
  GhnOrderShipInfoDataResponse data;
}
