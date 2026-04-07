package com.e_cormerce.shoppe.projection;

import java.time.LocalDateTime;

public interface ConversationLineProjection {
  String getId();

  String getOtherId();

  String getOtherAvatar();

  String getOtherName();

  String getLastSenderId();

  String getLastContent();

  LocalDateTime getLastContentAt();

  LocalDateTime getLastReadAt();
}