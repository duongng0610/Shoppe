package com.e_cormerce.shoppe.service.auth;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.dto.request.auth.login.LogInRequest;
import com.e_cormerce.shoppe.dto.request.auth.register.AbstractRegisterRequest;
import com.e_cormerce.shoppe.dto.request.auth.register.RegisterClientRequest;
import com.e_cormerce.shoppe.dto.request.auth.register.RegisterSellerRequest;
import com.e_cormerce.shoppe.dto.request.ghn.GhnCreateShopRequest;
import com.e_cormerce.shoppe.entity.product.ShoppingCart;
import com.e_cormerce.shoppe.entity.token.InvalidToken;
import com.e_cormerce.shoppe.entity.token.RefreshToken;
import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.entity.user.Role;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.user.AccountStatus;
import com.e_cormerce.shoppe.enums.user.RoleEnum;
import com.e_cormerce.shoppe.event.system.ClientRegistered;
import com.e_cormerce.shoppe.event.system.SellerRegistered;
import com.e_cormerce.shoppe.event.system.UserLoggedIn;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.repository.shopping_cart.ShoppingCartRepository;
import com.e_cormerce.shoppe.repository.token.InvalidTokenRepository;
import com.e_cormerce.shoppe.repository.token.RefreshTokenRepository;
import com.e_cormerce.shoppe.repository.user.AccountRepository;
import com.e_cormerce.shoppe.repository.user.RoleRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.address.AddressService;
import com.e_cormerce.shoppe.service.webclient.WebClientService;
import com.e_cormerce.shoppe.util.HashUtil;
import io.jsonwebtoken.Claims;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService {
  private final ShoppingCartRepository shoppingCartRepository;
  UserRepository userRepository;
  AccountRepository accountRepository;
  RoleRepository roleRepository;
  RefreshTokenRepository refreshTokenRepository;
  InvalidTokenRepository invalidTokenRepository;
  BCryptPasswordEncoder bCryptPasswordEncoder;
  JwtService jwtService;
  TokenService tokenService;
  UserMapper userMapper;
  JwtProperties jwtProperties;
  ApplicationEventPublisher eventPublisher;
  WebClientService webClientService;
  AddressService addressService;
  EntityManager entityManager;

  @Transactional()
  public String logIn(LogInRequest request) {
    Account account =
        accountRepository
            .findByEmail(request.getEmail())
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_ACCOUNT));

    boolean authenticated =
        bCryptPasswordEncoder.matches(request.getPassword(), account.getPassword());

    if (!authenticated) {
      throw new AppException(ErrorCode.INCORRECT_PASSWORD);
    }

    account.setStatus(AccountStatus.ACTIVE);
    accountRepository.save(account);
    eventPublisher.publishEvent(
        UserLoggedIn.builder()
            .date(LocalDateTime.now())
            .user(userMapper.toDto(account.getUser()))
            .build());

    return tokenService.generateAccessToken(account.getId(), account.getRole());
  }

  private User buildBaseUser(AbstractRegisterRequest request, RoleEnum roleEnum, String userId) {

    if (accountRepository.existsByEmail(request.getEmail())) {
      throw new AppException(ErrorCode.EXISTED_ACCOUNT);
    }

    if (userRepository.existsByUsername(request.getUsername())) {
      throw new AppException(ErrorCode.INVALID_USERNAME);
    }

    Role role =
        roleRepository
            .findByVal(roleEnum)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_ROLE));

    var account =
        Account.builder()
            .id(userId != null ? userId : UUID.randomUUID().toString())
            .email(request.getEmail())
            .password(bCryptPasswordEncoder.encode(request.getPassword()))
            .role(role)
            .status(AccountStatus.INACTIVE)
            .build();

    entityManager.persist(account);
    return User.builder()
        .account(account)
        .username(request.getUsername())
        .createdAt(LocalDateTime.now())
        .build();
  }

  @Transactional(timeout = 5)
  public void registerClient(RegisterClientRequest request) {

    var user = buildBaseUser(request, RoleEnum.CLIENT, null);

    entityManager.persist(user);
    //
    //    ClientStat clientStat = ClientStat.builder().client(user).build();
    //
    //    clientStatRepository.save(clientStat);

    ShoppingCart shoppingCart =
        ShoppingCart.builder().client(user).totalQuantity(0).name("Mặc định").build();

    shoppingCartRepository.save(shoppingCart);

    eventPublisher.publishEvent(
        ClientRegistered.builder()
            .date(LocalDateTime.now())
            .client(userMapper.toDto(user))
            .build());
  }

  @Transactional(timeout = 5)
  public void registerSeller(RegisterSellerRequest request) {
    // set info seller
    var addressDto = request.getAddress();
    var phoneNumber = request.getPhoneNumber();
    // lấy addressId để gọi GHN
    var address = addressService.getAddressByNames(addressDto);
    var res =
        webClientService.createShopGhnApi(
            GhnCreateShopRequest.builder()
                .name(request.getUsername())
                .address("...") // map lại cho đúng
                .districtId(address.getDistrictId())
                .wardCode(address.getWardId())
                .phone(phoneNumber)
                .build());

    var user = buildBaseUser(request, RoleEnum.SELLER, res.getData().getShopId());

    user.setPhoneNumber(phoneNumber);

    user.setAddress(address);

    entityManager.persist(user);

    eventPublisher.publishEvent(
        SellerRegistered.builder()
            .date(LocalDateTime.now())
            .seller(userMapper.toDto(user))
            .build());
  }

  @Transactional(timeout = 5)
  public void logOut(String accessToken) {
    revokeToken(accessToken);
    accountRepository.setStatus(getUserId(), AccountStatus.INACTIVE);
  }

  public UserDto verify() {
    UserDto res =
        userRepository
            .getUserDto(this.getUserId())
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));
    return res;
  }

  public User getUserThroughAuthentication() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    String userId = (String) authentication.getPrincipal();

    return userRepository
        .findById(userId)
        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));
  }

  public String getUserId() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return (String) authentication.getPrincipal();
  }

  private void revokeToken(String accessToken) {
    Claims accessTokenClaims =
        jwtService.extractClaims(accessToken, jwtProperties.getAccessTokenSecret());

    String refreshTokenId = (String) accessTokenClaims.get("refreshTokenId");

    InvalidToken invalidToken =
        InvalidToken.builder()
            .val(HashUtil.sha256(accessToken))
            .invalidDate(LocalDate.now())
            .build();

    invalidTokenRepository.save(invalidToken);

    RefreshToken refreshToken =
        refreshTokenRepository
            .findById(refreshTokenId)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_REFRESH_TOKEN));

    refreshToken.setRevoked(true);
    refreshTokenRepository.save(refreshToken);
  }
}
