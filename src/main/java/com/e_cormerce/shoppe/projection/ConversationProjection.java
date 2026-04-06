package com.e_cormerce.shoppe.projection;

import java.time.LocalDateTime;

public interface ConversationProjection {
  String getId();

  String getOtherId();

  String getOtherAvatar();

  LocalDateTime getCreatedAt();

  LocalDateTime getLastReadAt();
}
