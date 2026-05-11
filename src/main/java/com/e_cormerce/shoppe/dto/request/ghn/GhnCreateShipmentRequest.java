package com.e_cormerce.shoppe.dto.request.ghn;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GhnCreateShipmentRequest {

  Integer paymentTypeId = 1; // shop tra
  String requiredNote = "KHONGCHOXEMHANG";

  String toName;
  String toPhone;
  String toAddress;
  String toWardName;
  String toDistrictName;
  String toProvinceName;

  Integer serviceTypeId = 1;

  Integer length = 1;
  Integer width = 1;
  Integer height = 1;
  Integer weight = 1;

  List<ShipItemRequest> items;
}
