package com.e_cormerce.shoppe.dto.response.auth;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VerifyResponse {
    UserDto user;
    //....
}
