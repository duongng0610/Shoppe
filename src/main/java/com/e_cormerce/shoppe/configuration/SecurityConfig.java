package com.e_cormerce.shoppe.configuration;


import com.e_cormerce.shoppe.exception.filter.AccessDeninedException;
import com.e_cormerce.shoppe.exception.filter.JwtAuthEntryPoint;
import com.e_cormerce.shoppe.filter.AuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    private final AuthFilter authFilter;
    private final String[] PUBLIC_URLS = {
            "/",
            "/auth/log-in",
            "/auth/register",
            "/auth/log-out",
            "/upload-image"
    };


    public SecurityConfig(AuthFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AccessDeninedException accessDeninedException, JwtAuthEntryPoint jwtAuthEntryPoint) throws Exception {
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        .requestMatchers(PUBLIC_URLS).permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class)
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(e ->
                        e.authenticationEntryPoint(jwtAuthEntryPoint)
                                .accessDeniedHandler(accessDeninedException)
                );
        return http.build();
    }
}
