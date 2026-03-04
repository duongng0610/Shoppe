package com.e_cormerce.shoppe.configuration.helper;

import com.e_cormerce.shoppe.entity.user.Account;
import com.e_cormerce.shoppe.entity.user.Permission;
import com.e_cormerce.shoppe.entity.user.Role;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.PermissionEnum;
import com.e_cormerce.shoppe.enums.RoleEnum;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.AccountRepository;
import com.e_cormerce.shoppe.repository.PermissionRepository;
import com.e_cormerce.shoppe.repository.RoleRepository;
import com.e_cormerce.shoppe.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class AppConfigHelper {

    BCryptPasswordEncoder passwordEncoder;

    RoleRepository roleRepository;
    UserRepository userRepository;
    AccountRepository accountRepository;

    private Set<Permission> createPermissionsOfClient() {
        Set<Permission> clientPermissions = new HashSet<>();
        clientPermissions.add(Permission.builder().val(PermissionEnum.GET_INFO_CLIENT.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.GET_ORDERS_NOTIFICATIONS_CLIENT.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.GET_MESSAGES_NOTIFICATIONS_CLIENT.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.GET_CART_SHOPPING.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.ADD_CART_SHOPPING.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.DELETE_CART_ITEMS.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.GET_CART_SHOPPING_DELETED.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.RESTORE_CART_SHOPPING_DELETED.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.CREATE_ORDER.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.GET_SHIPPING_INFO.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.GET_CLIENT_ORDERS.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.PAY_ORDER.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.REQUEST_RETURN_ORDER.getVal()).build());
        clientPermissions.add(Permission.builder().val(PermissionEnum.REVIEW_RECEIVED_ORDER.getVal()).build());

        return clientPermissions;
    }

    private Set<Permission> createPermissionsOfSeller() {
        Set<Permission> sellerPermissions = new HashSet<>();
        sellerPermissions.add(Permission.builder().val(PermissionEnum.GET_SHOP_OVERVIEW.getVal()).build());
        sellerPermissions.add(Permission.builder().val(PermissionEnum.CREATE_PRODUCT.getVal()).build());
        sellerPermissions.add(Permission.builder().val(PermissionEnum.UPDATE_INFO_PRODUCT.getVal()).build());
        sellerPermissions.add(Permission.builder().val(PermissionEnum.DELETE_PRODUCT.getVal()).build());
        sellerPermissions.add(Permission.builder().val(PermissionEnum.GET_MY_PRODUCT.getVal()).build());
        sellerPermissions.add(Permission.builder().val(PermissionEnum.GET_STATUS_ORDER.getVal()).build());
        sellerPermissions.add(Permission.builder().val(PermissionEnum.ACCEPT_ORDER.getVal()).build());
        sellerPermissions.add(Permission.builder().val(PermissionEnum.APPROVE_RETURN_REQUEST.getVal()).build());

        return sellerPermissions;
    }

    private Set<Permission> createPermissionsOfAdmin() {
        Set<Permission> adminPermissions = new HashSet<>();

        adminPermissions.add(Permission.builder().val(PermissionEnum.GET_USERS_MANAGEMENT.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.GET_USER_DETAILS_MANAGEMENT.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.LOCK_USER.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.UNLOCK_USER.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.GET_PRODUCTS_MANAGEMENT.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.BAN_PRODUCT.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.UNBAN_PRODUCT.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.APPROVE_PRODUCTS.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.GET_CATEGORIES_MANAGEMENT.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.ADD_CATEGORY.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.GET_TRANSACTIONS.getVal()).build());
        adminPermissions.add(Permission.builder().val(PermissionEnum.GET_TRANSACTION_DETAILS.getVal()).build());

        return adminPermissions;
    }

    private Set<Permission> createPermissionsOfShipper() {
        Set<Permission> shipperPermissions = new HashSet<>();


        shipperPermissions.add(Permission.builder().val(PermissionEnum.GET_SHIPPER_INFO.getVal()).build());
        shipperPermissions.add(Permission.builder().val(PermissionEnum.GET_SHIPPER_OVERVIEW.getVal()).build());
        shipperPermissions.add(Permission.builder().val(PermissionEnum.GET_SUGGESTED_ORDER.getVal()).build());
        shipperPermissions.add(Permission.builder().val(PermissionEnum.CHECKOUT_SUGGESTED_ORDER.getVal()).build());
        shipperPermissions.add(Permission.builder().val(PermissionEnum.APPROVE_SUGGESTED_ORDER.getVal()).build());
        shipperPermissions.add(Permission.builder().val(PermissionEnum.GET_RECEIVED_ORDER.getVal()).build());
        shipperPermissions.add(Permission.builder().val(PermissionEnum.UPDATE_RECEIVED_ORDER.getVal()).build());
        shipperPermissions.add(Permission.builder().val(PermissionEnum.GET_SHIPPER_REVENUE.getVal()).build());

        return shipperPermissions;
    }

    public void createRoles() {
        if (!roleRepository.existsByVal(RoleEnum.CLIENT.getValue())) {
            Role clientRole = Role.builder()
                    .val(RoleEnum.CLIENT.getValue())
                    .permissions(createPermissionsOfClient()).build();
            roleRepository.save(clientRole);
        }
        if (!roleRepository.existsByVal(RoleEnum.SELLER.getValue())) {
            Role sellerRole = Role.builder()
                    .val(RoleEnum.SELLER.getValue())
                    .permissions(createPermissionsOfSeller()).build();
            roleRepository.save(sellerRole);
        }
        if (!roleRepository.existsByVal(RoleEnum.ADMIN.getValue())) {
            Role adminRole = Role.builder()
                    .val(RoleEnum.ADMIN.getValue())
                    .permissions(createPermissionsOfAdmin()).build();
            roleRepository.save(adminRole);
        }
        if (!roleRepository.existsByVal(RoleEnum.SHIPPER.getValue())) {
            Role shipperRole = Role.builder()
                    .val(RoleEnum.SHIPPER.getValue())
                    .permissions(createPermissionsOfShipper()).build();
            roleRepository.save(shipperRole);
        }
    }

    public void createAdmin(String email, String password, String username) {
            if(accountRepository.existsByEmail("admin")) {
                log.error("Email for admin is existed");
                throw new AppException(ErrorCode.EXISTED_ACCOUNT);
            }

            Account adminAccount = Account.builder()
                    .email(email)
                    .password(passwordEncoder.encode(password))
                    .created_at(new Date())
                    .build();

            Role adminRole = roleRepository.findByVal(RoleEnum.ADMIN.toString())
                    .orElseThrow(() -> new AppException(ErrorCode.INVALID_ROLE));

            if (userRepository.existsByUsername(username)) {
                log.error("Username for admin is invalid");
                throw new AppException(ErrorCode.INVALID_USERNAME);
            }

            User admin = User.builder()
                    .account(adminAccount)
                    .role(adminRole)
                    .username(username)
                    .build();

            userRepository.save(admin);

            log.warn("Admin has been created with email: -" + email
                    + "- password: -" + password
                    + "- username: -" + username + "-" );
    }
}
