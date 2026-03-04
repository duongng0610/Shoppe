package com.e_cormerce.shoppe.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "cookie.access-token")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component

public class CookieTokenProperties {
    int expirationTime;
}
