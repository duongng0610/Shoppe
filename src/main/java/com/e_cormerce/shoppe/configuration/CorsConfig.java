package com.e_cormerce.shoppe.configuration;

import com.e_cormerce.shoppe.properties.CorsProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@EnableConfigurationProperties({CorsProperties.class})
public class CorsConfig {
  CorsProperties corsProperties;

  @Bean
  public CorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration config = new CorsConfiguration();

    config.setAllowedOriginPatterns(corsProperties.getAllowedOrigins());
    config.setAllowedMethods(corsProperties.getAllowedMethods());
    config.setAllowedHeaders(corsProperties.getAllowedHeaders());
    config.setAllowCredentials(corsProperties.isCredentials());

    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

    source.registerCorsConfiguration("/**", config);

    return source;
  }
}
