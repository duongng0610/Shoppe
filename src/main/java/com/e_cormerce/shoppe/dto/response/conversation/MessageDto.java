package com.e_cormerce.shoppe.dto.response.conversation;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class MessageDto {
  String id;
  String content;
  String senderId;
  LocalDateTime createdAt;
  LocalDateTime updatedAt;
  List<MessageMediaDto> medias;
}
