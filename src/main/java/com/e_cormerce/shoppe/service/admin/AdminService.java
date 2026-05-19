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
import com.e_cormerce.shoppe.projection.overview.OverViewSystemProjection;
import com.e_cormerce.shoppe.projection.overview.OverviewOrderProductProjection;
import com.e_cormerce.shoppe.projection.user.UserDetailManageInfoProjection;
import com.e_cormerce.shoppe.projection.user.UserManageInfoProjection;
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

  // admin
  public OverviewOrderProductProjection getOverviewOrderProductForAdmin(Integer days) {

    return userRepository.getOverviewOrderProductForAdmin(days);
  }

  public List<UserManageInfoProjection> getClientInfo(int limit, int offset) {
    return userRepository.getClientInfo(limit, offset);
  }

  public UserDetailManageInfoProjection getInfoUser(String userId) {
    return userRepository.getInfoUser(userId);
  }

  public List<UserManageInfoProjection> getSellerInfo(int limit, int offset) {
    return userRepository.getSellerInfo(limit, offset);
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

  public OverViewSystemProjection getOverViewSystemForAdmin(Integer days) {
    return userRepository.getOverviewSystemForAdmin(days);
  }
}
