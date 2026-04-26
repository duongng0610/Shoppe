package com.e_cormerce.shoppe.enums.media;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public enum ImageType {
  PRODUCT("products/info"),
  AVATAR("users/avatars"),
  REVIEW("products/reviews"),
  CONVERSATION("conversations/images");

  String folderName;

  ImageType(String folderName) {
    this.folderName = folderName;
  }
}
