package com.e_cormerce.shoppe.projection.seller;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface SellerInformationProjection {
  String getSellerId();

  String getAvatar();

  String getAvatarId();

  LocalDateTime getCreatedAt();

  Boolean getDeleted();

  LocalDate getDob();

  String getPhoneNumber();

  LocalDateTime getUpdatedAt();

  String getUsername();

  String getAddressId();

  String getProvince();

  String getDistrict();

  String getWard();
}
