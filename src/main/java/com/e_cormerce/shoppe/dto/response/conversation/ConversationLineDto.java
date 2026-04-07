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
  String id;
  String otherId;
  String otherAvatar;
  String otherName;
  String lastContent;
  String lastContentAt;
  LocalDateTime lastReadAt;
}
