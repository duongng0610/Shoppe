package com.e_cormerce.shoppe.controller.home.auth;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.dto.request.auth.login.LogInRequest;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.properties.CookieTokenProperties;
import com.e_cormerce.shoppe.service.auth.AuthService;
//import com.e_cormerce.shoppe.util.ConvertObject;
import com.e_cormerce.shoppe.util.CookieUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthControllerTest {
    @Autowired MockMvc mockMvc;

    @MockitoBean AuthService authService;
    @MockitoBean CookieTokenProperties cookieTokenProperties;
    @MockitoBean CookieUtil cookieUtil;
    @Test
    void login_blankFields_returns400() throws Exception {
        String json = """
        {
          "email": "",
          "password": ""
        }
        """;

        mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").exists());

        Mockito.verify(authService, Mockito.never()).logIn(Mockito.any());
    }

    // =========================
    // 15
    // =========================
    @Test
    void registerClient_validRequest_returns201() throws Exception {
        String json = """
        {
          "email": "test@gmail.com",
          "password": "123456",
          "username": "testuser"
        }
        """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("register successfully"));

        Mockito.verify(authService).registerClient(Mockito.any());
    }

    // =========================
    // 16. Blank email
    // =========================
    @Test
    void registerClient_blankEmail_returns400() throws Exception {
        String json = """
        {
          "email": "",
          "password": "123456",
          "username": "testuser"
        }
        """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
    }

    // =========================
    // 17. Blank password
    // =========================
    @Test
    void registerClient_blankPassword_returns400() throws Exception {
        String json = """
        {
          "email": "test@gmail.com",
          "password": "",
          "username": "testuser"
        }
        """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
    }

    // =========================
    // 18. Blank username
    // =========================
    @Test
    void registerClient_blankUsername_returns400() throws Exception {
        String json = """
        {
          "email": "test@gmail.com",
          "password": "123456",
          "username": ""
        }
        """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
    }

    // =========================
    // 19. Email existed
    // =========================
    @Test
    void registerClient_emailExists_returnsError() throws Exception {
        String json = """
        {
          "email": "test@gmail.com",
          "password": "123456",
          "username": "testuser"
        }
        """;

        Mockito.doThrow(new AppException(ErrorCode.EXISTED_ACCOUNT))
                .when(authService).registerClient(Mockito.any());

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService).registerClient(Mockito.any());
    }

    // =========================
    // 20. Username invalid
    // =========================
    @Test
    void registerClient_invalidUsername_returnsError() throws Exception {
        String json = """
        {
          "email": "test@gmail.com",
          "password": "123456",
          "username": "testuser"
        }
        """;

        Mockito.doThrow(new AppException(ErrorCode.INVALID_USERNAME))
                .when(authService).registerClient(Mockito.any());

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService).registerClient(Mockito.any());
    }

    // =========================
    // 21. Missing body
    // =========================
    @Test
    void registerClient_missingBody_returns400() throws Exception {
        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
    }
}
