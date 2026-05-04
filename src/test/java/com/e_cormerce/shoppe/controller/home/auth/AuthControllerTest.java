package com.e_cormerce.shoppe.controller.home.auth;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.dto.request.auth.login.LogInRequest;
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
@AutoConfigureMockMvc
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

    private final String VALID_BODY = """
            {
              "email": "seller@gmail.com",
              "password": "123456",
              "username": "seller1",
              "phoneNumber": "0123456789",
              "address": {}
            }
            """;
    //B. Register Seller
    // =========================
    // 1. SUCCESS
    // =========================
    @Test
    void registerSeller_success() throws Exception {

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(VALID_BODY))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("register successfully"));

        Mockito.verify(authService).registerSeller(Mockito.any());
    }

    // =========================
    // 2. SERVICE THROW EXCEPTION
    // =========================
    @Test
    void registerSeller_serviceThrow_returns500() throws Exception {

        Mockito.doThrow(new RuntimeException("error"))
                .when(authService).registerSeller(Mockito.any());

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(VALID_BODY))
                .andExpect(status().isInternalServerError());

        Mockito.verify(authService).registerSeller(Mockito.any());
    }

    // =========================
    // 3. MISSING BODY
    // =========================
    @Test
    void registerSeller_missingBody_returns400() throws Exception {

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json"))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }

    // =========================
    // 4. INVALID JSON
    // =========================
    @Test
    void registerSeller_invalidJson_returns400() throws Exception {

        String invalidBody = "{ invalid json }";

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(invalidBody))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }

    // =========================
    // 5. EMPTY BODY
    // =========================
    @Test
    void registerSeller_emptyBody_returns400() throws Exception {

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(""))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }

    // =========================
    // 6. MISSING REQUIRED FIELD (email)
    // =========================
    @Test
    void registerSeller_missingEmail_returns400() throws Exception {

        String body = """
                {
                  "password": "123456",
                  "username": "seller1",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }

    // =========================
    // 7. MULTIPLE CALL VERIFY
    // =========================
    @Test
    void registerSeller_calledTwice() throws Exception {

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(VALID_BODY))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(VALID_BODY))
                .andExpect(status().isCreated());

        Mockito.verify(authService, Mockito.times(2))
                .registerSeller(Mockito.any());
    }
    // =========================
    // 8.INVALID EMAIL FORMAT
    // =========================
    @Test
    void registerSeller_invalidEmail_returns400() throws Exception {

        String body = """
                {
                  "email": "invalid-email",
                  "password": "123456",
                  "username": "seller1",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }

    // =========================
    // 9. PASSWORD TOO SHORT
    // =========================
    @Test
    void registerSeller_shortPassword_returns400() throws Exception {

        String body = """
                {
                  "email": "seller@gmail.com",
                  "password": "123",
                  "username": "seller1",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }

    // =========================
    // 10. USERNAME TOO SHORT
    // =========================
    @Test
    void registerSeller_shortUsername_returns400() throws Exception {

        String body = """
                {
                  "email": "seller@gmail.com",
                  "password": "123456",
                  "username": "a",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }

    // =========================
    // 11. PHONE INVALID FORMAT
    // =========================
    @Test
    void registerSeller_invalidPhone_returns400() throws Exception {

        String body = """
                {
                  "email": "seller@gmail.com",
                  "password": "123456",
                  "username": "seller1",
                  "phoneNumber": "abc123",
                  "address": {}
                }
                """;

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }

    // =========================
    // 12. EMAIL BLANK
    // =========================
    @Test
    void registerSeller_blankEmail_returns400() throws Exception {

        String body = """
                {
                  "email": "",
                  "password": "123456",
                  "username": "seller1",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }

    // =========================
    // 13. PASSWORD BLANK
    // =========================
    @Test
    void registerSeller_blankPassword_returns400() throws Exception {

        String body = """
                {
                  "email": "seller@gmail.com",
                  "password": "",
                  "username": "seller1",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }

    // =========================
    // 14. MULTIPLE INVALID FIELDS
    // =========================
    @Test
    void registerSeller_multipleInvalidFields_returns400() throws Exception {

        String body = """
                {
                  "email": "invalid",
                  "password": "1",
                  "username": "",
                  "phoneNumber": "abc",
                  "address": {}
                }
                """;

        mockMvc.perform(post("/auth/register/seller")
                        .contentType("application/json")
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerSeller(Mockito.any());
    }
}
