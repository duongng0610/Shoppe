package com.e_cormerce.shoppe.service.account;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import com.e_cormerce.shoppe.dto.request.account.ChangePasswordRequest;
import com.e_cormerce.shoppe.dto.request.account.ChangeShipInfo;
import com.e_cormerce.shoppe.dto.request.account.ChangeUserProfileRequest;
import com.e_cormerce.shoppe.dto.response.account.ChangeShipInfoResponse;
import com.e_cormerce.shoppe.dto.response.account.ChangeUserProfileResponse;
import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.address.AddressMapper;
import com.e_cormerce.shoppe.repository.user.AddressRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.account.helper.AccountServiceHelper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@Log4j2
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AccountService {
  UserRepository userRepository;
  BCryptPasswordEncoder bCryptPasswordEncoder;
  AccountServiceHelper accountServiceHelper;
  AddressRepository addressRepository;
  AddressMapper addressMapper;

  public void changePassword(ChangePasswordRequest request) {
    User user = accountServiceHelper.getUser();
    Account account = accountServiceHelper.getAccount(user);

    if (!accountServiceHelper.isTrueOldPassword(request.getOldPassword(), account)) {
      throw new AppException(ErrorCode.INCORRECT_PASSWORD);
    }

    accountServiceHelper.updatePassword(request.getNewPassword(), account);
  }

  public ChangeUserProfileResponse updateProfile(
      ChangeUserProfileRequest request, MultipartFile avatar) {
    User user = accountServiceHelper.getUser();

    accountServiceHelper.applyProfileChanges(user, request);
    accountServiceHelper.applyAvatarChange(user, avatar);

    userRepository.save(user);

    return ChangeUserProfileResponse.builder()
        .dob(user.getDob())
        .avatar(user.getAvatar())
        .username(user.getUsername())
        .build();
  }

  public ChangeShipInfoResponse updateShipInfo(ChangeShipInfo request) {
    User user = accountServiceHelper.getUser();

    if (request.getAddress() != null) {
      AddressDto newAddress = request.getAddress();
      accountServiceHelper.updateAddress(newAddress, user);
    }

    if (request.getPhoneNumber() != null && !request.getPhoneNumber().isBlank()) {
      accountServiceHelper.updatePhoneNumber(request.getPhoneNumber(), user);
    }

    userRepository.save(user);

    return ChangeShipInfoResponse.builder()
        .phoneNumber(user.getPhoneNumber())
        .address(addressMapper.toDto(user.getAddress()))
        .build();
  }
}
