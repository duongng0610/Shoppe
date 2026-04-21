package com.e_cormerce.shoppe.event.system;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserLoggedIn {
    LocalDateTime date;
    UserDto user;
}
