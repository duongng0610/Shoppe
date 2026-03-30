package com.e_cormerce.shoppe.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse<T> {
  @Builder.Default int code = 1000;
  @Builder.Default boolean success = true;
  String message;
  T data;
}
