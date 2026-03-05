package com.e_cormerce.shoppe.configuration;


import com.e_cormerce.shoppe.exception.filter.AccessDeninedException;
import com.e_cormerce.shoppe.exception.filter.JwtAuthEntryPoint;
import com.e_cormerce.shoppe.filter.AuthFilter;
import com.e_cormerce.shoppe.properties.EndpointProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableConfigurationProperties({EndpointProperties.class})
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SecurityConfig {


    AuthFilter authFilter;

    EndpointProperties endpointProperties;


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AccessDeninedException accessDeninedException, JwtAuthEntryPoint jwtAuthEntryPoint) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers( endpointProperties.getPublicUrls().toArray(new String[0])).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())//nếu trong ioc có bean CorsConfiguration sẽ dùng bean đó
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(jwtAuthEntryPoint)
                                .accessDeniedHandler(accessDeninedException)
                );
        return http.build();
    }


}
