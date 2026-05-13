package com.e_cormerce.shoppe.projection.user;

import com.e_cormerce.shoppe.entity.user.Role;

import java.security.Timestamp;

public interface UserManageInfoProjection {
    String getId();

    String getUsername();

    String getAvatar();

    Timestamp getCreatedAt();

    String getStatus();

    String getEmail();

    Role getRole();
}
