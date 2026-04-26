package com.e_cormerce.shoppe.service.media.validator;

import com.e_cormerce.shoppe.enums.media.ImageType;

public interface ImageValidator {
  ImageType getSupportedType();

  boolean isUsed(String imageId);
}
