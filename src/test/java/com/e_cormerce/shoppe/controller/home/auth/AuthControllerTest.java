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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
@ActiveProfiles("test")
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

    // 2. Blank email
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

    // 3. Blank password
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

    // 4. Blank username
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

    // 5. Email existed
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

    // 6. Username invalid
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

    // 7. Missing body
    @Test
    void registerClient_missingBody_returns400() throws Exception {
        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
    }

    // =========================
// 8. INVALID EMAIL FORMAT
// =========================
    @Test
    void registerClient_invalidEmail_returns400() throws Exception {

        String json = """
    {
      "email": "invalid-email",
      "password": "123456",
      "username": "testuser"
    }
    """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerClient(Mockito.any());
    }

    // =========================
// 9. PASSWORD TOO SHORT
// =========================
    @Test
    void registerClient_shortPassword_returns400() throws Exception {

        String json = """
    {
      "email": "test@gmail.com",
      "password": "123",
      "username": "testuser"
    }
    """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerClient(Mockito.any());
    }

    // =========================
// 10. USERNAME TOO SHORT
// =========================
    @Test
    void registerClient_shortUsername_returns400() throws Exception {

        String json = """
    {
      "email": "test@gmail.com",
      "password": "123456",
      "username": "a"
    }
    """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerClient(Mockito.any());
    }

    // =========================
// 11. EMPTY BODY
// =========================
    @Test
    void registerClient_emptyBody_returns400() throws Exception {

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerClient(Mockito.any());
    }

    // =========================
// 12. INVALID JSON
// =========================
    @Test
    void registerClient_invalidJson_returns400() throws Exception {

        String invalidJson = "{ invalid json }";

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerClient(Mockito.any());
    }

    // =========================
// 13. MULTIPLE INVALID FIELDS
// =========================
    @Test
    void registerClient_multipleInvalidFields_returns400() throws Exception {

        String json = """
    {
      "email": "invalid",
      "password": "1",
      "username": ""
    }
    """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerClient(Mockito.any());
    }

    // =========================
// 14. REGISTER CLIENT SUCCESS CALLED TWICE
// =========================
    @Test
    void registerClient_calledTwice() throws Exception {

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
                .andExpect(status().isCreated());

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        Mockito.verify(authService, Mockito.times(2))
                .registerClient(Mockito.any());
    }

    // =========================
// 15. SERVICE THROW RUNTIME EXCEPTION
// =========================
    @Test
    void registerClient_serviceThrow_returns500() throws Exception {

        String json = """
    {
      "email": "test@gmail.com",
      "password": "123456",
      "username": "testuser"
    }
    """;

        Mockito.doThrow(new RuntimeException("server error"))
                .when(authService)
                .registerClient(Mockito.any());

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isInternalServerError());

        Mockito.verify(authService)
                .registerClient(Mockito.any());
    }

    // =========================
// 16. EMAIL FIELD MISSING
// =========================
    @Test
    void registerClient_missingEmail_returns400() throws Exception {

        String json = """
    {
      "password": "123456",
      "username": "testuser"
    }
    """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerClient(Mockito.any());
    }

    // =========================
// 17. PASSWORD FIELD MISSING
// =========================
    @Test
    void registerClient_missingPassword_returns400() throws Exception {

        String json = """
    {
      "email": "test@gmail.com",
      "username": "testuser"
    }
    """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerClient(Mockito.any());
    }

    // =========================
// 18. USERNAME FIELD MISSING
// =========================
    @Test
    void registerClient_missingUsername_returns400() throws Exception {

        String json = """
    {
      "email": "test@gmail.com",
      "password": "123456"
    }
    """;

        mockMvc.perform(post("/auth/register/client")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        Mockito.verify(authService, Mockito.never())
                .registerClient(Mockito.any());
    }
}
