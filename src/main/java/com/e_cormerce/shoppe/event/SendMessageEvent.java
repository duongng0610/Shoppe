package com.e_cormerce.shoppe.event;

import com.e_cormerce.shoppe.entity.user.User;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendMessageEvent {
  String messageId;
  String content;
  User user;
  LocalDateTime createdAt;
}
