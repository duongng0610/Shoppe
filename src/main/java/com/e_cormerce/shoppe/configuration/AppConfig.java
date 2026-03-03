package com.e_cormerce.shoppe.configuration;

import com.e_cormerce.shoppe.entity.user.Permission;
import com.e_cormerce.shoppe.entity.user.Role;
import com.e_cormerce.shoppe.enums.PermissionEnum;
import com.e_cormerce.shoppe.enums.RoleEnum;
import com.e_cormerce.shoppe.repository.PermissionRepository;
import com.e_cormerce.shoppe.repository.RoleRepository;
import com.e_cormerce.shoppe.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AppConfig {

    RoleRepository roleRepository;
    PermissionRepository permissionRepository;
    UserRepository userRepository;
    PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner initData() {
        return args -> {


            //=========Role=======
            if (!roleRepository.existsByVal(RoleEnum.CLIENT.getValue())) {
                Role clientRole = Role.builder().val(RoleEnum.CLIENT.getValue()).permissions(createPermissionsOfClient()).build();
                roleRepository.save(clientRole);
            }
            if (!roleRepository.existsByVal(RoleEnum.SELLER.getValue())) {
                Role sellerRole = Role.builder().val(RoleEnum.SELLER.getValue()).permissions(createPermissionsOfSeller()).build();
                roleRepository.save(sellerRole);
            }
            if (!roleRepository.existsByVal(RoleEnum.ADMIN.getValue())) {
                Role adminRole = Role.builder().val(RoleEnum.ADMIN.getValue()).permissions(createPermissionsOfAdmin()).build();
                roleRepository.save(adminRole);
            }
            if (!roleRepository.existsByVal(RoleEnum.SHIPPER.getValue())) {
                Role shipperRole = Role.builder().val(RoleEnum.SHIPPER.getValue()).permissions(createPermissionsOfShipper()).build();
                roleRepository.save(shipperRole);
            }

        };
    }

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

}
