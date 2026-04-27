package com.e_cormerce.shoppe.dto.request.ghn;

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
public class GhnCalculateShipFeeRequest {

  private Integer serviceTypeId;
  private Integer toDistrictId;
  private String toWardCode;
  private Integer length;
  private Integer width;
  private Integer height;
  private Integer weight;
  private Integer insuranceValue;
}
