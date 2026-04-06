package com.e_cormerce.shoppe.dto.response.chat;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import java.time.LocalDateTime;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageResponse {
  UserDto from;

  String text;

  List<String> imageUrls;

  LocalDateTime createdAt;
}
