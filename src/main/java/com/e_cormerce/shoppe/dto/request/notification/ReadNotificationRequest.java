package com.e_cormerce.shoppe.dto.request.notification;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReadNotificationRequest {
  @NotEmpty(message = "Danh sách id không được để trống")
  @Size(min = 1)
  List<String> ids;
}
