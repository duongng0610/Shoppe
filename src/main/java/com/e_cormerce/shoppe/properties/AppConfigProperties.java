package com.e_cormerce.shoppe.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "config")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component

public class AppConfigProperties {
    String admin_email;
    String admin_password;
    String admin_username;

    String seller_email;
    String seller_password;
    String seller_username;
}
