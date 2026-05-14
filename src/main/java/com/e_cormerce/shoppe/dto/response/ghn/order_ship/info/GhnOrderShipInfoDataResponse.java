package com.e_cormerce.shoppe.dto.response.ghn.order_ship.info;

import com.e_cormerce.shoppe.dto.request.ghn.ShipItem;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
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

    String pickupTime;

    String leadtime;

    String createdDate;


    String finishDate;

    String returnTime;

    String fromName;

    String fromPhone;

    String fromAddress;

    String toName;

    String toPhone;

    String toAddress;

    List<GhnOrderShipTag> tag;

    List<ShipItem> items;
}

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
class GhnOrderShipTag {
    String status;
    Integer paymentTypeId;
    LocalDateTime updatedAt;
}