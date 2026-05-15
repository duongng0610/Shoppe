package com.e_cormerce.shoppe.dto.response.ghn.order_ship.info;

import com.e_cormerce.shoppe.dto.request.ghn.ShipItem;
import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnOrderShipInfoDataResponse {

    @JsonAlias("order_code")
    private String orderCode;

    private String status;

    @JsonAlias("pickup_time")
    private LocalDateTime pickupTime;

    @JsonAlias("created_date")
    private LocalDateTime createdDate;

    @JsonAlias("finish_date")
    private LocalDateTime finishDate;

    @JsonAlias("return_time")
    private LocalDateTime returnTime;

    @JsonAlias("from_name")
    private String fromName;

    @JsonAlias("from_phone")
    private String fromPhone;

    @JsonAlias("from_address")
    private String fromAddress;

    @JsonAlias("to_name")
    private String toName;

    @JsonAlias("to_phone")
    private String toPhone;

    @JsonAlias("to_address")
    private String toAddress;


    List<GhnOrderShipTag> log;

    List<ShipItem> items;
}

