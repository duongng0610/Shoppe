package com.e_cormerce.shoppe.configuration;

import com.e_cormerce.shoppe.configuration.helper.AppConfigHelper;
import com.e_cormerce.shoppe.properties.AdminProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AppConfig {

  AppConfigHelper appConfigHelper;
  AdminProperties appConfigProperties;

  @Bean
  CommandLineRunner initData() {
    return args -> {
      appConfigHelper.createRoles();

      appConfigHelper.createAdmin(
          appConfigProperties.getEmail(),
          appConfigProperties.getPassword(),
          appConfigProperties.getUsername());
      appConfigHelper.createDefaultCategories();
//      appConfigHelper.initAddressData();
    };
  }
}
