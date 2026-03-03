package com.e_cormerce.shoppe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)

public class LogInResponse {
    String email;
    String hashedPassword;
    String accessToken;
}
