package com.e_cormerce.shoppe.projection.user;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface UserDetailManageInfoProjection {
    String getId();

    String getEmail();

    String getUsername();

    String getAvatar();

    String getRole();

    String getPhoneNumber();

    LocalDate getBirthDate();

    LocalDateTime getCreatedAt();

    String getProvince();

    String getDistrict();

    String getWard();
}
