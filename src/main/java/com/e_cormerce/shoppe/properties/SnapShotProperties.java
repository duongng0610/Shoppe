package com.e_cormerce.shoppe.properties;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties(prefix = "app.snapshot")
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component

public class SnapShotProperties {
    long delay;
    String filePath;
}
