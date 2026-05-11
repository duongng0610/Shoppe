package com.e_cormerce.shoppe.projection;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;

public interface UserDetailManageInfoProjection {
  String getId();

  String getUsername();

  String getAvatar();

  String getPhoneNumber();

  String getStatus();

  String getCreatedAt();

  AddressDto getAddress();
}
