package com.e_cormerce.shoppe.dto.response.ghn.order_ship.info;


import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GhnOrderShipInfoDataResponse {

    String orderCode;

    String status;

    String sortCode;

    String pickupTime;

    String leadtime;

    String createdDate;

    String updatedDate;

    String finishDate;

    String returnTime;

    String fromName;

    String fromPhone;

    String fromAddress;

    String toName;

    String toPhone;

    String toAddress;

    Integer currentWarehouseId;

    Integer nextWarehouseId;

    Integer pickWarehouseId;

    Integer deliverWarehouseId;

    Integer returnWarehouseId;

    Integer weight;

    Integer length;

    Integer width;

    Integer height;

    Integer codAmount;

    String content;

    List<String> tag;


}