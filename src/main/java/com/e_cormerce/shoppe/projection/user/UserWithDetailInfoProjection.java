package com.e_cormerce.shoppe.projection.user;

import java.util.Date;

public interface UserWithDetailInfoProjection {

    String getId();

    String getEmail();

    String getUsername();

    String getAvatar();

    String getRole();

    String getPhoneNumber();

    Date getBirthDate();

    Date getCreatedAt();

    String getProvince();

    String getDistrict();

    String getWard();
}
