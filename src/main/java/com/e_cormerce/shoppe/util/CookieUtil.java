package com.e_cormerce.shoppe.util;

import com.e_cormerce.shoppe.properties.CookieTokenProperties;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Component
public class CookieUtil {
  CookieTokenProperties cookieTokenProperties;

  public String getAccessToken(HttpServletRequest request) {
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

  public void saveToken(String name, String value, int age, HttpServletResponse response) {
    var cookie =
        ResponseCookie.from(name, value)
            .httpOnly(cookieTokenProperties.isHttpOnly())
            .maxAge(age)
            .sameSite(cookieTokenProperties.getSameSite())
            .secure(cookieTokenProperties.isSecure())
            .path(cookieTokenProperties.getPath())
            .build();
    response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
  }
}
