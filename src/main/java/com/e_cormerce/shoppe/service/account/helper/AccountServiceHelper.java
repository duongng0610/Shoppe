package com.e_cormerce.shoppe.service.account.helper;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import com.e_cormerce.shoppe.dto.request.account.ChangeUserProfileRequest;
import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.entity.user.Address;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.user.AccountRepository;
import com.e_cormerce.shoppe.repository.user.AddressRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.media.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceHelper {
  AccountRepository accountRepository;
  AddressRepository addressRepository;
  UserRepository userRepository;
  AuthService authService;
  BCryptPasswordEncoder bCryptPasswordEncoder;
  ImageService imageService;

  public Account getAccount(User user) {
    return accountRepository.findAccountByUserId(user.getId());
  }

  public User getUser() {
    return authService.getUserThroughAuthentication();
  }

  public boolean isTrueOldPassword(String oldPassword, Account account) {
    return bCryptPasswordEncoder.matches(oldPassword, account.getPassword());
  }

  public void updatePassword(String newPassword, Account account) {
    account.setPassword(bCryptPasswordEncoder.encode(newPassword));
    accountRepository.save(account);
  }

  public void applyProfileChanges(User user, ChangeUserProfileRequest request) {
    if (request == null) return;

    if (request.getDob() != null) {
      user.setDob(request.getDob());
    }

    String newUsername = request.getUsername();
    if (newUsername != null && !newUsername.isBlank()) {
      if (userRepository.existsByUsername(newUsername)) {
        throw new AppException(ErrorCode.EXISTED_USERNAME);
      }
      user.setUsername(newUsername);
    }
  }

  public void applyAvatarChange(User user, MultipartFile avatar) {
    if (avatar == null || avatar.isEmpty()) return;

    String newAvatar = imageService.uploadSingleImage(avatar);
    user.setAvatar(newAvatar);
  }

  public void updateAddress(AddressDto newAddress, User user) {
    Address address =
        addressRepository
            .findByEntireAddress(
                newAddress.getProvince(), newAddress.getDistrict(), newAddress.getWard())
            .orElseGet(
                () -> {
                  Address addAddress =
                      Address.builder()
                          .province(newAddress.getProvince())
                          .district(newAddress.getDistrict())
                          .ward(newAddress.getWard())
                          .build();
                  return addressRepository.save(addAddress);
                });

    user.setAddress(address);
  }

  public void updatePhoneNumber(String phoneNumber, User user) {
    user.setPhoneNumber(phoneNumber);
  }
}
