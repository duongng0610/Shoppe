package com.e_cormerce.shoppe.configuration.helper;

import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.category.CategorySynonyms;
import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.entity.user.Permission;
import com.e_cormerce.shoppe.entity.user.Role;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.user.RoleEnum;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.catgory.CategoryRepository;
import com.e_cormerce.shoppe.repository.catgory.SynonymsRepository;
import com.e_cormerce.shoppe.repository.user.AccountRepository;
import com.e_cormerce.shoppe.repository.user.PermissionRepository;
import com.e_cormerce.shoppe.repository.user.RoleRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.util.constants.DefaultCategory;
import com.e_cormerce.shoppe.util.constants.RoleValue;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppConfigHelper {

  BCryptPasswordEncoder passwordEncoder;

  RoleRepository roleRepository;
  UserRepository userRepository;
  AccountRepository accountRepository;
  CategoryRepository categoryRepository;
  SynonymsRepository synonymsRepository;
  PermissionRepository permissionRepository;

  ApplicationEventPublisher eventPublisher;

  @Transactional
  public void createRoles() {
    RoleValue.ROLE_PERMISSIONS.forEach(
        (roleVal, permissionVals) -> {
          Role role =
              roleRepository
                  .findByVal(roleVal)
                  .orElseGet(() -> Role.builder().val(roleVal).build());
          permissionVals.forEach(
              permissionVal -> {
                Permission permission =
                    permissionRepository
                        .findByVal(permissionVal)
                        .orElseGet(() -> Permission.builder().val(permissionVal).build());
                if (role.getPermissions() == null
                    || (role.getPermissions() != null
                        && !role.getPermissions().contains(permission))) {
                  role.addPermission(permission);
                }
              });
          roleRepository.save(role);
        });
  }

  public void createAdmin(String email, String password, String username) {
    if (!accountRepository.existsByEmail(email)) {
      Role adminRole =
          roleRepository
              .findByVal(RoleEnum.ADMIN)
              .orElseThrow(() -> new AppException(ErrorCode.INVALID_ROLE));

      Account adminAccount =
          Account.builder()
              .email(email)
              .password(passwordEncoder.encode(password))
              .role(adminRole)
              .build();

      if (userRepository.existsByUsername(username)) {
        log.error("Username for admin is invalid");
        throw new AppException(ErrorCode.INVALID_USERNAME);
      }

      User admin = User.builder().account(adminAccount).username(username).build();

      userRepository.save(admin);
    }
  }

  public void createDefaultCategories() {
    var categories = DefaultCategory.DEFAULT_CATEGORIES;
    for (int i = 0; i < categories.length; i++) {
      String name = categories[i][0];
      String thumbnail = categories[i][1];
      if (!categoryRepository.existsByVal(name)) {
        Category category = Category.builder().val(name).thumbnail(thumbnail).build();
        categoryRepository.save(category);
        synonymsRepository.save(CategorySynonyms.builder().val(name).category(category).build());
      }
    }
  }
}
