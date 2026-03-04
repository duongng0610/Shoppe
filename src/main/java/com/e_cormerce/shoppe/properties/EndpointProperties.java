package com.e_cormerce.shoppe.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "app.security.endpoints")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EndpointProperties {
    List<String> publicUrls;
}
