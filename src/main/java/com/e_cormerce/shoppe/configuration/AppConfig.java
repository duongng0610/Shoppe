package com.e_cormerce.shoppe.configuration;

import com.e_cormerce.shoppe.configuration.helper.AppConfigHelper;
import com.e_cormerce.shoppe.properties.AdminProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@RequiredArgsConstructor
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
@Profile("!test")
public class AppConfig {

    AppConfigHelper appConfigHelper;
    AdminProperties appConfigProperties;
    SqlInitConfig sqlInitConfig;

    @Bean
    CommandLineRunner initData(@Qualifier("masterDataSource") DataSource dataSource) {
        return args -> {
            sqlInitConfig.initDatabase(dataSource);
            appConfigHelper.createRoles();
            appConfigHelper.createAdmin(
                    appConfigProperties.getEmail(),
                    appConfigProperties.getPassword(),
                    appConfigProperties.getUsername());
            appConfigHelper.createDefaultCategories();
            appConfigHelper.createDefaultAddress();
        };
    }
}
