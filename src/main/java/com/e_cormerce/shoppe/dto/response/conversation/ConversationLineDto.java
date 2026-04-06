package com.e_cormerce.shoppe.dto.response.conversation;

import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConversationLineDto {
  Object id;
  Object otherId;
  Object otherAvatar;
  LocalDateTime createdAt;
  LocalDateTime lastReadAt;
}
