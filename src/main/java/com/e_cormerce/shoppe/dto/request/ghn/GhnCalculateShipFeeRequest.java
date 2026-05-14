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

    private Integer serviceTypeId = 2;
    private Integer fromDistrictId;
    private String fromWardCode;
    private Integer toDistrictId;
    private String toWardCode;
    private Integer length = 1;
    private Integer width = 1;
    private Integer height = 1;
    private Integer weight = 1;
    private Integer insuranceValue = 0;
}
