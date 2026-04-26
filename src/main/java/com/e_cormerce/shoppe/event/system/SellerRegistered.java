package com.e_cormerce.shoppe.event.system;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class SellerRegistered {
  LocalDateTime date;
  UserDto seller;
}
