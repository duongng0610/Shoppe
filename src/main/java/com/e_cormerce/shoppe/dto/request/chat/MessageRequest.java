package com.e_cormerce.shoppe.dto.request.chat;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageRequest {
  String text;

  @NotBlank String conversationId;

  List<String> imageUrls;
}
