package com.e_cormerce.shoppe.exception.filter;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class AccessDeninedException implements AccessDeniedHandler {
  @Override
  public void handle(
      HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException)
      throws IOException, ServletException {
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType("application/json");
    response.setCharacterEncoding("UTF-8");
    ObjectMapper mapper = new ObjectMapper();
    response
        .getWriter()
        .write(
            mapper.writeValueAsString(
                ApiResponse.builder()
                    .success(false)
                    .message("Không có quyền truy cập endpoint này")
                    .build()));
  }
}
