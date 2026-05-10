package com.e_cormerce.shoppe.service.admin;

import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.product.ProductStatus;
import com.e_cormerce.shoppe.enums.user.AccountStatus;
import com.e_cormerce.shoppe.event.product.ProductApproved;
import com.e_cormerce.shoppe.event.product.ProductBanned;
import com.e_cormerce.shoppe.event.product.ProductRejected;
import com.e_cormerce.shoppe.event.product.ProductUnlocked;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.product.ProductMapper;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.projection.UserDetailManageInfoProjection;
import com.e_cormerce.shoppe.projection.UserManageInfoProjection;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.repository.user.AccountRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@EnableConfigurationProperties({JwtProperties.class})
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AdminService {
  ProductRepository productRepository;
  ApplicationEventPublisher eventPublisher;
  ProductMapper productMapper;
  UserMapper userMapper;
  UserRepository userRepository;
  AccountRepository accountRepository;

  @Transactional
  public void approveProducts(String productId) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_PRODUCT));
    if (product.getStatus() != ProductStatus.PENDING) {
      throw new AppException(ErrorCode.UNABLE_APPROVE_PRODUCT);
    }
    productRepository.updateStatus(productId, ProductStatus.ACTIVE.getValue());
    eventPublisher.publishEvent(
        ProductApproved.builder()
            .product(productMapper.toProductCardDto(product))
            .seller(userMapper.toDto(product.getSeller()))
            .build());
  }

  @Transactional
  public void rejectProducts(String productId) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_PRODUCT));

    if (product.getStatus() != ProductStatus.PENDING) {
      throw new AppException(ErrorCode.UNABLE_APPROVE_PRODUCT);
    }

    productRepository.updateStatus(productId, ProductStatus.REJECTED.getValue());

    eventPublisher.publishEvent(
        ProductRejected.builder()
            .product(productMapper.toProductCardDto(product))
            .seller(userMapper.toDto(product.getSeller()))
            .build());
  }

  @Transactional
  public void banProducts(String productId) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_PRODUCT));

    if (product.getStatus() != ProductStatus.ACTIVE) {
      throw new AppException(ErrorCode.UNABLE_BAN_PRODUCT);
    }

    productRepository.updateStatus(productId, ProductStatus.BANNED.getValue());

    eventPublisher.publishEvent(
        ProductBanned.builder()
            .product(productMapper.toProductCardDto(product))
            .seller(userMapper.toDto(product.getSeller()))
            .build());
  }

  @Transactional
  public void unlockProducts(String productId) {
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_PRODUCT));

    if (product.getStatus() != ProductStatus.BANNED) {
      throw new AppException(ErrorCode.UNABLE_UNLOCK_PRODUCT);
    }

    productRepository.updateStatus(productId, ProductStatus.ACTIVE.getValue());

    eventPublisher.publishEvent(
        ProductUnlocked.builder()
            .product(productMapper.toProductCardDto(product))
            .seller(userMapper.toDto(product.getSeller()))
            .build());
  }

  public List<UserManageInfoProjection> getClientInfo(int limit, int offset) {
    return userRepository.getClientInfo(limit, offset);
  }

  public UserDetailManageInfoProjection getClientDetailInfo(String clientId) {
    return userRepository.getClientDetailInfo(clientId);
  }

  public UserDetailManageInfoProjection getSellerDetailInfo(String sellerId) {
    return userRepository.getRegisteredSellerDetailInfo(sellerId);
  }

  public List<UserManageInfoProjection> getRegisteredSellerInfo(int limit, int offset) {
    return userRepository.getRegisteredSellerInfo(limit, offset);
  }

  public List<UserManageInfoProjection> getUnregisteredSellerInfo(int limit, int offset) {
    return userRepository.getUnregisteredSellerInfo(limit, offset);
  }

  @Transactional
  public void approveSellerRegister(String sellerId) {
    Account account =
        accountRepository
            .findById(sellerId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));
    if (account.getStatus() != AccountStatus.PENDING) {
      throw new AppException(ErrorCode.UNABLE_APPROVE_SELLER_REGISTER);
    }
    accountRepository.setStatus(sellerId, AccountStatus.INACTIVE);
  }

  @Transactional
  public void rejectSellerRegister(String sellerId) {
    Account account =
        accountRepository
            .findById(sellerId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));
    if (account.getStatus() != AccountStatus.PENDING) {
      throw new AppException(ErrorCode.UNABLE_REJECT_SELLER_REGISTER);
    }
    accountRepository.setStatus(sellerId, AccountStatus.REJECTED);
  }

  @Transactional
  public void banUser(String userId) {
    Account account =
        accountRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));

    if (account.getStatus() != AccountStatus.INACTIVE
        && account.getStatus() != AccountStatus.ACTIVE) {
      throw new AppException((ErrorCode.UNABLE_BAN_USER));
    }

    accountRepository.setStatus(userId, AccountStatus.BANNED);
  }

  @Transactional
  public void unbanUser(String userId) {
    Account account =
        accountRepository
            .findById(userId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));

    if (account.getStatus() != AccountStatus.BANNED) {
      throw new AppException((ErrorCode.UNABLE_UNBAN_USER));
    }

    accountRepository.setStatus(userId, AccountStatus.INACTIVE);
  }
}
