package com.e_cormerce.shoppe.event;

import com.e_cormerce.shoppe.entity.user.User;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Getter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SendMessageEvent {
    String messageId;
    String content;
    User user;
    LocalDateTime createdAt;
}
