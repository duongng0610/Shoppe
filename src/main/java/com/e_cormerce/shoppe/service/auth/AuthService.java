package com.e_cormerce.shoppe.service.auth;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.dto.request.auth.login.LogInRequest;
import com.e_cormerce.shoppe.dto.request.auth.register.AbstractRegisterRequest;
import com.e_cormerce.shoppe.dto.request.auth.register.RegisterSellerRequest;
import com.e_cormerce.shoppe.dto.request.auth.register.RegisterShipperRequest;
import com.e_cormerce.shoppe.entity.product.ShoppingCart;
import com.e_cormerce.shoppe.entity.seller.SellerInfo;
import com.e_cormerce.shoppe.entity.token.InvalidToken;
import com.e_cormerce.shoppe.entity.token.RefreshToken;
import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.entity.user.Role;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.user.RoleEnum;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.repository.seller.SellerInfoRepository;
import com.e_cormerce.shoppe.repository.shopping_cart.ShoppingCartRepository;
import com.e_cormerce.shoppe.repository.token.InvalidTokenRepository;
import com.e_cormerce.shoppe.repository.token.RefreshTokenRepository;
import com.e_cormerce.shoppe.repository.user.AccountRepository;
import com.e_cormerce.shoppe.repository.user.RoleRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.util.HashUtil;
import io.jsonwebtoken.Claims;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthService {
    private final ShoppingCartRepository shoppingCartRepository;
    SellerInfoRepository sellerInfoRepository;
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


        return tokenService.generateAccessToken(account.getId(), account.getRole());
    }

    @Transactional(timeout = 5)
    public void registerUser(AbstractRegisterRequest request, RoleEnum roleEnum) {
        if (accountRepository.existsByEmail(request.getEmail())) {
            throw new AppException(ErrorCode.EXISTED_ACCOUNT);
        }


        Role role =
                roleRepository
                        .findByVal(roleEnum.getValue())
                        .orElseThrow(() -> new AppException(ErrorCode.INVALID_ROLE));

        var account =
                Account.builder()
                        .email(request.getEmail())
                        .password(bCryptPasswordEncoder.encode(request.getPassword()))
                        .role(role)
                        .build();

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.INVALID_USERNAME);
        }

        var user =
                User.builder()
                        .account(account)
                        .username(request.getUsername())
                        .createdAt(LocalDateTime.now())
                        .build();

        if (request.getClass() == RegisterSellerRequest.class
                || request.getClass() == RegisterShipperRequest.class) {

            var addressDto =
                    request.getClass() == RegisterShipperRequest.class
                            ? ((RegisterShipperRequest) request).getAddress()
                            : ((RegisterSellerRequest) request).getAddress();

            var phoneNumber = request.getClass() == RegisterShipperRequest.class
                    ? ((RegisterShipperRequest) request).getPhoneNumber()
                    : null;

            user.setProvince(addressDto.getProvince());
            user.setDistrict(addressDto.getDistrict());
            user.setWard(addressDto.getWard());
            user.setPhoneNumber(phoneNumber);
        }

        userRepository.save(user);
        if (role.getVal().equals(RoleEnum.CLIENT.getValue())) {
            ShoppingCart shoppingCart = ShoppingCart.builder().client(user).totalQuantity(0).build();
            shoppingCartRepository.save(shoppingCart);
        } else if (role.getVal().equals(RoleEnum.SELLER.getValue())) {
            SellerInfo sellerInfo =
                    SellerInfo.builder().seller(user).build();
            sellerInfoRepository.save(sellerInfo);
        }
    }

    @Transactional(timeout = 5)
    public void logOut(String accessToken) {

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

    public UserDto verify() {
        UserDto res = userRepository.getUserDto(this.getUserId()).orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));
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
}
