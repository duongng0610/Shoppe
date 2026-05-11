package com.e_cormerce.shoppe.dto.response.ghn.order_ship;

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
public class GhnCreateShipmentDataResponse {
  String orderCode;
  String sortCode;
  String transType;
  String wardEncode;
  String districtEncode;
  String totalFee;
  String expectedDeliveryTime;
}
