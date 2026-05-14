package com.e_cormerce.shoppe.projection.user;

import java.time.LocalDateTime;

public interface UserManageInfoProjection {
    String getId();

    String getUsername();

    String getAvatar();

    LocalDateTime getCreatedAt();

    String getStatus();

    String getEmail();

    String getRole();
}
