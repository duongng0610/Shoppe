package com.e_cormerce.shoppe.controller.home.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.controller.auth.AuthController;
import com.e_cormerce.shoppe.dto.request.auth.login.LogInRequest;
import com.e_cormerce.shoppe.properties.CookieTokenProperties;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.util.ConvertObject;
import com.e_cormerce.shoppe.util.CookieUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthControllerTest {
  @Autowired MockMvc mockMvc;

  @MockitoBean AuthService authService;
  @MockitoBean CookieTokenProperties cookieTokenProperties;
  @MockitoBean CookieUtil cookieUtil;

  @Test
  void login_blankFields_returns400_withValidationMessage() throws Exception {
    var request = LogInRequest.builder().email("").password("").build();

    mockMvc
        .perform(
            post("/auth/login")
                .content(ConvertObject.toJson(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("LoginRequest must not be blank"));
  }

  @Test
  void login_invalidEmail_returns400() throws Exception {
    var request = LogInRequest.builder().email("not-an-email").password("Seller@123").build();

    mockMvc
        .perform(
            post("/auth/login")
                .content(ConvertObject.toJson(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid Email"));
  }

  @Test
  void login_weakPassword_returns400() throws Exception {
    var request = LogInRequest.builder().email("van@gmail.com").password("123").build();

    mockMvc
        .perform(
            post("/auth/login")
                .content(ConvertObject.toJson(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").value("Invalid Password"));
  }

  @Test
  void login_valid_callsService_andSetsCookie_returns200() throws Exception {
    var request = LogInRequest.builder().email("van@gmail.com").password("Vant1@abc").build();
    Mockito.when(authService.logIn(Mockito.any())).thenReturn("token");
    Mockito.when(cookieTokenProperties.getExpirationTime()).thenReturn(3600);

    mockMvc
        .perform(
            post("/auth/login")
                .content(ConvertObject.toJson(request))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("login successfully"));

    Mockito.verify(authService).logIn(Mockito.any());
    Mockito.verify(cookieUtil)
        .saveToken(
            Mockito.eq("access_token"), Mockito.eq("token"), Mockito.eq(3600), Mockito.any());
  }
}
