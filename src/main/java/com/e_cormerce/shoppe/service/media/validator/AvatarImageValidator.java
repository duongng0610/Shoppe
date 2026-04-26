package com.e_cormerce.shoppe.service.media.validator;

import com.e_cormerce.shoppe.enums.media.ImageType;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AvatarImageValidator implements ImageValidator {
  UserRepository userRepository;

  @Override
  public ImageType getSupportedType() {
    return ImageType.AVATAR;
  }

  @Override
  public boolean isUsed(String imageId) {
    return userRepository.existsByAvatarId(imageId);
  }
}
