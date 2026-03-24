package com.e_cormerce.shoppe.controller.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.dto.request.auth.login.LogInRequest;
import com.e_cormerce.shoppe.properties.CookieTokenProperties;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.util.ConvertObject;
import com.e_cormerce.shoppe.util.CookieUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

/**
 * ===Cách 1: load full context. @SpringBootTest(webEnvironment =
 * SpringBootTest.WebEnvironment.MOCK) @AutoConfigureMockMvc//bắt buộc thêm nếu dùng SpringBootTest
 * + MockMvc để testController run mock server: fake http and can catch controller endpoint.
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
// bắt buộc thêm nếu dùng SpringBootTest + MockMvc để testController
/**
 * Cách 2:nhẹ hơn, tập trung vào class controller duy nhất nếu dùng WebMvcTest cần thêm thuộc tính
 * addFilters = false để bỏ qua tầng filter. @WebMvcTest(controllers =
 * AuthController.class) @AutoConfigureMockMvc(addFilters = false)
 */
// =====common=====
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthControllerTest {
  @Autowired MockMvc mockMvc;

  @MockitoBean AuthService authService;

  @MockitoBean CookieTokenProperties cookieTokenProperties;

  @MockitoBean CookieUtil cookieUtil;

  @Test
  public void testLoginRequestEmptyField() throws Exception {
    var request = LogInRequest.builder().email("123").password("123").build();

    mockMvc
        .perform(
            post("/auth/login")
                .content(ConvertObject.toJson(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("LoginRequest must not be blank"));
  }

  @Test
  public void testLoginRequestInvalidPassword() throws Exception {
    var request = LogInRequest.builder().email("123").password("123").build();

    mockMvc
        .perform(
            post("/auth/login")
                .content(ConvertObject.toJson(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid Password"));
  }

  @Test
  public void testLoginRequestInvalidEmail() throws Exception {
    var request = LogInRequest.builder().email("123").password("Seller@123").build();

    mockMvc
        .perform(
            post("/auth/login")
                .content(ConvertObject.toJson(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid Email"));
  }

  @Test
  public void testLoginRequestValid() throws Exception {
    var request = LogInRequest.builder().email("van@gmail.com").password("Vant1@abc").build();

    mockMvc
        .perform(
            post("/auth/login")
                .content(ConvertObject.toJson(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk());
  }
}
