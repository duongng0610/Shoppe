package com.e_cormerce.shoppe.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import com.e_cormerce.shoppe.dto.request.auth.login.LogInRequest;
import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.address.AddressMapper;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.repository.token.InvalidTokenRepository;
import com.e_cormerce.shoppe.repository.token.RefreshTokenRepository;
import com.e_cormerce.shoppe.repository.user.AccountRepository;
import com.e_cormerce.shoppe.repository.user.AddressRepository;
import com.e_cormerce.shoppe.repository.user.RoleRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.auth.JwtService;
import com.e_cormerce.shoppe.service.auth.TokenService;
import java.util.Optional;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Tách tầng repository. Khi test tầng service(bussiness logic ~ không cần chạy server, chỉ cần test
 * chức năng ExtendWith thuộc junit , không phải của spring nên ko có load context. Dùng Mock cho
 * các thuộc tính cần thêm lớp service phụ thuộc và InjectMock cho lớp service test
 */
@ExtendWith(MockitoExtension.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthServiceTest {
  @InjectMocks AuthService authService;

  @Mock UserRepository userRepository;

  @Mock AccountRepository accountRepository;

  @Mock RoleRepository roleRepository;

  @Mock RefreshTokenRepository refreshTokenRepository;

  @Mock InvalidTokenRepository invalidTokenRepository;

  @Mock AddressMapper addressMapper;

  @Mock BCryptPasswordEncoder bCryptPasswordEncoder;

  @Mock JwtService jwtService;

  @Mock TokenService tokenService;

  @Mock UserMapper userMapper;

  @Mock JwtProperties jwtProperties;

  @Mock AddressRepository addressRepository;

  @Test
  public void successLogin() {
    // 1.Arrange data
    var request = LogInRequest.builder().email("aaaa").password("aaaa").build();
    var account = Account.builder().email("aaaa").password("aaaa").build();
    var user = new User();
    var result = "token";
    account.setUser(user);

    when(accountRepository.findByEmail(request.getEmail())).thenReturn(Optional.of(account));

    when(bCryptPasswordEncoder.matches(request.getPassword(), account.getPassword()))
        .thenReturn(true);

    when(tokenService.generateAccessToken(user)).thenReturn("token");
    // 2.Act
    var token = authService.logIn(request);
    // 3.Assert
    assertEquals(token, result);
  }

  @Test
  void logIn_accountNotFound() {
    // Arrange
    LogInRequest request = new LogInRequest("email@gmail.com", "123");

    when(accountRepository.findByEmail("email@gmail.com")).thenReturn(Optional.empty());

    // Act + Assert
    assertThrows(
        AppException.class,
        () -> {
          authService.logIn(request);
        });
  }

  @Test
  void logIn_wrongPassword() {
    // Arrange
    LogInRequest request = new LogInRequest("email@gmail.com", "123");

    Account account = new Account();
    account.setPassword("hashedPassword");

    when(accountRepository.findByEmail("email@gmail.com")).thenReturn(Optional.of(account));

    when(bCryptPasswordEncoder.matches("123", "hashedPassword")).thenReturn(false);

    // Act + Assert
    AppException ex =
        assertThrows(
            AppException.class,
            () -> {
              authService.logIn(request);
            });

    assertEquals(ErrorCode.INCORRECT_PASSWORD, ex.getErrorCode());
  }
}
