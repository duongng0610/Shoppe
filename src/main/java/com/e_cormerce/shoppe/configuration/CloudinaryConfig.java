package com.e_cormerce.shoppe.configuration;

import com.cloudinary.Cloudinary;
import com.e_cormerce.shoppe.properties.CloudinaryProperties;
import java.util.HashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableConfigurationProperties({CloudinaryProperties.class})
public class CloudinaryConfig {
  CloudinaryProperties cloudinaryProperties;

  @Bean
  public Cloudinary getCloudinary() {
    Map config = new HashMap();
    config.put("cloud_name", cloudinaryProperties.getCloudName());
    config.put("api_key", cloudinaryProperties.getApiKey());
    config.put("api_secret", cloudinaryProperties.getSecretKey());
    config.put("secure", true);
    return new Cloudinary(config);
  }
}
