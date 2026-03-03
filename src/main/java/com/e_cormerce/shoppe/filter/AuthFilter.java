package com.e_cormerce.shoppe.filter;

import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.repository.InvalidTokenRepository;
import com.e_cormerce.shoppe.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@EnableConfigurationProperties({JwtProperties.class})
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthFilter extends OncePerRequestFilter {


    JwtService jwtService;
    InvalidTokenRepository invalidTokenRepository;
    JwtProperties jwtProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String accessToken = extractTokenFromCookie(request);
            if (accessToken != null) {//nếu ko tìm thấy accessToken thì nhảy xuống luôn doFilter
                if (invalidTokenRepository.existsByVal(accessToken)) {
                    throw new BadCredentialsException("Token is invalid!");//ném lỗi này SpringSecurity tự động nhảy vào phần xử lí AuthenEntryPoint
                }
                jwtService.decode(accessToken, jwtProperties.getAccessTokenSecret());
                Authentication authentication = jwtService.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (Exception ex) {
            throw new BadCredentialsException(ex.getMessage(), ex);//ném lỗi này SpringSecurity tự động nhảy vào phần xử lí AuthenEntryPoint
        }
    }

    private String extractTokenFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()) {
            if ("access_token".equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
