package com.e_cormerce.shoppe.configuration;

import com.e_cormerce.shoppe.configuration.helper.AppConfigHelper;
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
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j

public class AppConfig {

    AppConfigHelper appConfigHelper;

    @Bean
    CommandLineRunner initData() {
        return args -> {

            appConfigHelper.createRoles();
            appConfigHelper.createAdmin("admin", "admin", "admin");

        };
    }

}
