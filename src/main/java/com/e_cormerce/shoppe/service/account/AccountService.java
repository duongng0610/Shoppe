package com.e_cormerce.shoppe.service.account;

import com.e_cormerce.shoppe.dto.request.account.ChangePasswordRequest;
import com.e_cormerce.shoppe.dto.request.account.ChangeUserProfileRequest;
import com.e_cormerce.shoppe.dto.response.account.ChangeUserProfileResponse;
import com.e_cormerce.shoppe.dto.response.account.UserProfileResponse;
import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.account.helper.AccountServiceHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
  UserRepository userRepository;
  BCryptPasswordEncoder bCryptPasswordEncoder;
  AccountServiceHelper accountServiceHelper;

  public void changePassword(ChangePasswordRequest request) {
    User user = accountServiceHelper.getUser();
    Account account = accountServiceHelper.getAccount(user);

    if (!accountServiceHelper.isTrueOldPassword(request.getOldPassword(), account)) {
      throw new AppException(ErrorCode.INCORRECT_PASSWORD);
    }

    accountServiceHelper.updatePassword(request.getNewPassword(), account);
  }

  public ChangeUserProfileResponse updateProfile(ChangeUserProfileRequest request) {
    User user = accountServiceHelper.getUser();

    accountServiceHelper.applyProfileChanges(user, request);
    accountServiceHelper.applyAvatarChange(user, request.getThumbnailUrl());

    userRepository.save(user);

    return ChangeUserProfileResponse.builder()
        .dob(user.getDob())
        .avatar(user.getAvatar())
        .username(user.getUsername())
        .build();
  }

  public UserProfileResponse getUserProfile() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    System.out.println(authentication.getPrincipal());
    return userRepository
        .getUserProfileById(authentication.getPrincipal().toString())
        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));
  }
}
