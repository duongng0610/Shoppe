package com.e_cormerce.shoppe.dto.response.ghn.order_ship.info;

import com.fasterxml.jackson.annotation.JsonAlias;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GhnOrderShipTag {
  String status;

  @JsonAlias("updated_at")
  LocalDateTime updatedAt;
}
