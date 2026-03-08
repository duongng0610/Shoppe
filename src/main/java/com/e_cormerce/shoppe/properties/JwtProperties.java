package com.e_cormerce.shoppe.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

@ConfigurationProperties(prefix = "jwt")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@ComponentScan
public class JwtProperties {
  String accessTokenSecret;
  String refreshTokenSecret;
  Long accessTokenExpirationTime;
  Long refreshTokenExpirationTime;
}
