package com.e_cormerce.shoppe.service.account.helper;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import com.e_cormerce.shoppe.dto.request.account.ChangeUserProfileRequest;
import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.user.AccountRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.address.AddressService;
import com.e_cormerce.shoppe.service.auth.AuthService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountServiceHelper {
  AccountRepository accountRepository;
  UserRepository userRepository;
  AuthService authService;
  BCryptPasswordEncoder bCryptPasswordEncoder;
  AddressService addressService;

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
    System.out.println(request.getDob());
    if (request == null) return;

    if (request.getDob() != null) {
      user.setDob(request.getDob());
    }
    if (request.getUsername() != null && !request.getUsername().isBlank()) {
      if (userRepository.existsByUsername(request.getUsername())) {
        throw new AppException(ErrorCode.EXISTED_USERNAME);
      }
      user.setUsername(request.getUsername());
    }

    if (request.getAddress() != null) {
      AddressDto newAddress = request.getAddress();
      this.updateAddress(newAddress, user);
    }

    if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
      this.updatePhoneNumber(request.getPhoneNumber(), user);
    }
  }

  public void applyAvatarChange(User user, String avatarUrl) {
    if (avatarUrl == null || avatarUrl.isBlank()) return;
    user.setAvatar(avatarUrl);
  }

  public void updateAddress(AddressDto newAddress, User user) {
    user.setAddress(addressService.getAddressByNames(newAddress));
  }

  public void updatePhoneNumber(String phoneNumber, User user) {
    user.setPhoneNumber(phoneNumber);
  }
}
