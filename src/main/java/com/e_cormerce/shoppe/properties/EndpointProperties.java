package com.e_cormerce.shoppe.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

@ConfigurationProperties(prefix = "app.security.endpoints")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component

public class EndpointProperties {
    List<String> publicUrls;
}
