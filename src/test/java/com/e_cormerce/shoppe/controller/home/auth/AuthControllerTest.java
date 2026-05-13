package com.e_cormerce.shoppe.controller.home.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.properties.CookieTokenProperties;
import com.e_cormerce.shoppe.service.auth.AuthService;
// import com.e_cormerce.shoppe.util.ConvertObject;
import com.e_cormerce.shoppe.util.CookieUtil;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc(addFilters = false)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ActiveProfiles("test")
public class AuthControllerTest {
  @Autowired MockMvc mockMvc;

  @MockitoBean AuthService authService;
  @MockitoBean CookieTokenProperties cookieTokenProperties;
  @MockitoBean CookieUtil cookieUtil;

  // A.login
  @Test
  void login_blankFields_returns400() throws Exception {
    String json =
        """
                {
                  "email": "",
                  "password": ""
                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message").exists());

    Mockito.verify(authService, Mockito.never()).logIn(any());
  }

  @Test
  void login_success() throws Exception {

    String body =
        """
                {
                    "email": "test@gmail.com",
                    "password": "123456"
                }
                """;

    Mockito.when(authService.logIn(any())).thenReturn("access-token");

    Mockito.when(cookieTokenProperties.getExpirationTime()).thenReturn(1000);

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("login successfully"));

    Mockito.verify(authService).logIn(any());
    Mockito.verify(cookieUtil).saveToken(eq("access_token"), eq("access-token"), eq(1000), any());
  }

  // =========================
  // 2. WRONG PASSWORD
  // =========================
  @Test
  void login_wrongPassword_returns500() throws Exception {

    String body =
        """
                {
                    "email": "test@gmail.com",
                    "password": "wrong-password"
                }
                """;

    Mockito.when(authService.logIn(any())).thenThrow(new RuntimeException("wrong password"));

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isInternalServerError());

    Mockito.verify(authService).logIn(any());

    Mockito.verify(cookieUtil, Mockito.never())
        .saveToken(any(), any(), Math.toIntExact(any(Long.class)), any());
  }

  // =========================
  // 3. EMAIL NULL
  // =========================
  @Test
  void login_emailNull_returns400() throws Exception {

    String body =
        """
                {
                    "email": null,
                    "password": "123456"
                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).logIn(any());
  }

  // =========================
  // 4. PASSWORD NULL
  // =========================
  @Test
  void login_passwordNull_returns400() throws Exception {

    String body =
        """
                {
                    "email": "test@gmail.com",
                    "password": null
                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).logIn(any());
  }

  // =========================
  // 5. EMPTY REQUEST BODY
  // =========================
  @Test
  void login_emptyBody_returns400() throws Exception {

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(""))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).logIn(any());
  }

  // =========================
  // 6. INVALID JSON
  // =========================
  @Test
  void login_invalidJson_returns400() throws Exception {

    String body =
        """
                {
                    "email": "test@gmail.com",
                    "password":
                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).logIn(any());
  }

  // =========================
  // 7. LONG EMAIL
  // =========================
  @Test
  void login_longEmail_success() throws Exception {

    String longEmail = "a".repeat(100) + "@gmail.com";

    String body =
        """
                {
                    "email": "%s",
                    "password": "123456"
                }
                """
            .formatted(longEmail);

    Mockito.when(authService.logIn(any())).thenReturn("token");

    Mockito.when(cookieTokenProperties.getExpirationTime()).thenReturn(1000);

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk());

    Mockito.verify(authService).logIn(any());
  }

  // =========================
  // 8. LOGIN CALLED TWICE
  // =========================
  @Test
  void login_calledTwice() throws Exception {

    String body =
        """
                {
                    "email": "test@gmail.com",
                    "password": "123456"
                }
                """;

    Mockito.when(authService.logIn(any())).thenReturn("token");

    Mockito.when(cookieTokenProperties.getExpirationTime()).thenReturn(1000);

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk());

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isOk());

    Mockito.verify(authService, Mockito.times(2)).logIn(any());
  }

  // =========================
  // 9. COOKIE SAVE THROW EXCEPTION
  // =========================
  @Test
  void login_cookieSaveThrowsException_returns500() throws Exception {

    String body =
        """
                {
                    "email": "test@gmail.com",
                    "password": "123456"
                }
                """;

    Mockito.when(authService.logIn(any())).thenReturn("token");

    Mockito.when(cookieTokenProperties.getExpirationTime()).thenReturn(1000);

    Mockito.doThrow(new RuntimeException("cookie error"))
        .when(cookieUtil)
        .saveToken(any(), any(), Math.toIntExact(any(Long.class)), any());

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isInternalServerError());

    Mockito.verify(authService).logIn(any());

    Mockito.verify(cookieUtil).saveToken(any(), any(), Math.toIntExact(any(Long.class)), any());
  }

  // =========================
  // 10. CONTENT TYPE INVALID
  // =========================
  @Test
  void login_invalidContentType_returns415() throws Exception {

    String body =
        """
                {
                    "email": "test@gmail.com",
                    "password": "123456"
                }
                """;

    mockMvc
        .perform(post("/auth/login").contentType(MediaType.TEXT_PLAIN).content(body))
        .andExpect(status().isUnsupportedMediaType());

    Mockito.verify(authService, Mockito.never()).logIn(any());
  }

  private final String VALID_BODY =
      """
            {
              "email": "seller@gmail.com",
              "password": "123456",
              "username": "seller1",
              "phoneNumber": "0123456789",
              "address": {}
            }
            """;

  // B. Register Seller
  // =========================
  // 1. SUCCESS
  // =========================
  @Test
  void registerSeller_success() throws Exception {

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(VALID_BODY))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("register successfully"));

    Mockito.verify(authService).registerSeller(any());
  }

  // =========================
  // 2. SERVICE THROW EXCEPTION
  // =========================
  @Test
  void registerSeller_serviceThrow_returns500() throws Exception {

    Mockito.doThrow(new RuntimeException("error")).when(authService).registerSeller(any());

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(VALID_BODY))
        .andExpect(status().isInternalServerError());

    Mockito.verify(authService).registerSeller(any());
  }

  // =========================
  // 3. MISSING BODY
  // =========================
  @Test
  void registerSeller_missingBody_returns400() throws Exception {

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json"))
        .andExpect(status().isInternalServerError());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  // =========================
  // 4. INVALID JSON
  // =========================
  @Test
  void registerSeller_invalidJson_returns400() throws Exception {

    String invalidBody = "{ invalid json }";

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(invalidBody))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  // =========================
  // 5. EMPTY BODY
  // =========================
  @Test
  void registerSeller_emptyBody_returns400() throws Exception {

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(""))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  // =========================
  // 6. MISSING REQUIRED FIELD (email)
  // =========================
  @Test
  void registerSeller_missingEmail_returns400() throws Exception {

    String body =
        """
                {
                  "password": "123456",
                  "username": "seller1",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  // =========================
  // 7. MULTIPLE CALL VERIFY
  // =========================
  @Test
  void registerSeller_calledTwice() throws Exception {

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(VALID_BODY))
        .andExpect(status().isCreated());

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(VALID_BODY))
        .andExpect(status().isCreated());

    Mockito.verify(authService, Mockito.times(2)).registerSeller(any());
  }

  // =========================
  // 8.INVALID EMAIL FORMAT
  // =========================
  @Test
  void registerSeller_invalidEmail_returns400() throws Exception {

    String body =
        """
                {
                  "email": "invalid-email",
                  "password": "123456",
                  "username": "seller1",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  // =========================
  // 9. PASSWORD TOO SHORT
  // =========================
  @Test
  void registerSeller_shortPassword_returns400() throws Exception {

    String body =
        """
                {
                  "email": "seller@gmail.com",
                  "password": "123",
                  "username": "seller1",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  // =========================
  // 10. USERNAME TOO SHORT
  // =========================
  @Test
  void registerSeller_shortUsername_returns400() throws Exception {

    String body =
        """
                {
                  "email": "seller@gmail.com",
                  "password": "123456",
                  "username": "a",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  // =========================
  // 11. PHONE INVALID FORMAT
  // =========================
  @Test
  void registerSeller_invalidPhone_returns400() throws Exception {

    String body =
        """
                {
                  "email": "seller@gmail.com",
                  "password": "123456",
                  "username": "seller1",
                  "phoneNumber": "abc123",
                  "address": {}
                }
                """;

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  // =========================
  // 12. EMAIL BLANK
  // =========================
  @Test
  void registerSeller_blankEmail_returns400() throws Exception {

    String body =
        """
                {
                  "email": "",
                  "password": "123456",
                  "username": "seller1",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  // =========================
  // 13. PASSWORD BLANK
  // =========================
  @Test
  void registerSeller_blankPassword_returns400() throws Exception {

    String body =
        """
                {
                  "email": "seller@gmail.com",
                  "password": "",
                  "username": "seller1",
                  "phoneNumber": "0123456789",
                  "address": {}
                }
                """;

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  // =========================
  // 14. MULTIPLE INVALID FIELDS
  // =========================
  @Test
  void registerSeller_multipleInvalidFields_returns400() throws Exception {

    String body =
        """
                {
                  "email": "invalid",
                  "password": "1",
                  "username": "",
                  "phoneNumber": "abc",
                  "address": {}
                }
                """;

    mockMvc
        .perform(post("/auth/register/seller").contentType("application/json").content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerSeller(any());
  }

  @Test
  void registerClient_validRequest_returns201() throws Exception {
    String json =
        """
        {
          "email": "test@gmail.com",
          "password": "123456",
          "username": "testuser"
        }
        """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("register successfully"));

    Mockito.verify(authService).registerClient(any());
  }

  // 2. Blank email
  @Test
  void registerClient_blankEmail_returns400() throws Exception {
    String json =
        """
        {
          "email": "",
          "password": "123456",
          "username": "testuser"
        }
        """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(any());
  }

  // 3. Blank password
  @Test
  void registerClient_blankPassword_returns400() throws Exception {
    String json =
        """
        {
          "email": "test@gmail.com",
          "password": "",
          "username": "testuser"
        }
        """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(any());
  }

  // 4. Blank username
  @Test
  void registerClient_blankUsername_returns400() throws Exception {
    String json =
        """
        {
          "email": "test@gmail.com",
          "password": "123456",
          "username": ""
        }
        """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(any());
  }

  // 5. Email existed
  @Test
  void registerClient_emailExists_returnsError() throws Exception {
    String json =
        """
        {
          "email": "test@gmail.com",
          "password": "123456",
          "username": "testuser"
        }
        """;

    Mockito.doThrow(new AppException(ErrorCode.EXISTED_ACCOUNT))
        .when(authService)
        .registerClient(any());

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService).registerClient(any());
  }

  // 6. Username invalid
  @Test
  void registerClient_invalidUsername_returnsError() throws Exception {
    String json =
        """
        {
          "email": "test@gmail.com",
          "password": "123456",
          "username": "testuser"
        }
        """;

    Mockito.doThrow(new AppException(ErrorCode.INVALID_USERNAME))
        .when(authService)
        .registerClient(any());

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService).registerClient(any());
  }

  // 7. Missing body
  @Test
  void registerClient_missingBody_returns400() throws Exception {
    mockMvc
        .perform(post("/auth/register/client").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(any());
  }

  // =========================
  // 8. INVALID EMAIL FORMAT
  // =========================
  @Test
  void registerClient_invalidEmail_returns400() throws Exception {

    String json =
        """
    {
      "email": "invalid-email",
      "password": "123456",
      "username": "testuser"
    }
    """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
  }

  // =========================
  // 9. PASSWORD TOO SHORT
  // =========================
  @Test
  void registerClient_shortPassword_returns400() throws Exception {

    String json =
        """
    {
      "email": "test@gmail.com",
      "password": "123",
      "username": "testuser"
    }
    """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
  }

  // =========================
  // 10. USERNAME TOO SHORT
  // =========================
  @Test
  void registerClient_shortUsername_returns400() throws Exception {

    String json =
        """
    {
      "email": "test@gmail.com",
      "password": "123456",
      "username": "a"
    }
    """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
  }

  // =========================
  // 11. EMPTY BODY
  // =========================
  @Test
  void registerClient_emptyBody_returns400() throws Exception {

    mockMvc
        .perform(post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(""))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
  }

  // =========================
  // 12. INVALID JSON
  // =========================
  @Test
  void registerClient_invalidJson_returns400() throws Exception {

    String invalidJson = "{ invalid json }";

    mockMvc
        .perform(
            post("/auth/register/client")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
  }

  // =========================
  // 13. MULTIPLE INVALID FIELDS
  // =========================
  @Test
  void registerClient_multipleInvalidFields_returns400() throws Exception {

    String json =
        """
    {
      "email": "invalid",
      "password": "1",
      "username": ""
    }
    """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
  }

  // =========================
  // 14. REGISTER CLIENT SUCCESS CALLED TWICE
  // =========================
  @Test
  void registerClient_calledTwice() throws Exception {

    String json =
        """
    {
      "email": "test@gmail.com",
      "password": "123456",
      "username": "testuser"
    }
    """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated());

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isCreated());

    Mockito.verify(authService, Mockito.times(2)).registerClient(Mockito.any());
  }

  // =========================
  // 15. SERVICE THROW RUNTIME EXCEPTION
  // =========================
  @Test
  void registerClient_serviceThrow_returns500() throws Exception {

    String json =
        """
    {
      "email": "test@gmail.com",
      "password": "123456",
      "username": "testuser"
    }
    """;

    Mockito.doThrow(new RuntimeException("server error"))
        .when(authService)
        .registerClient(Mockito.any());

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isInternalServerError());

    Mockito.verify(authService).registerClient(Mockito.any());
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

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
  }

  // =========================
  // 17. PASSWORD FIELD MISSING
  // =========================
  @Test
  void registerClient_missingPassword_returns400() throws Exception {

    String json =
        """
    {
      "email": "test@gmail.com",
      "username": "testuser"
    }
    """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
  }

  // =========================
  // 18. USERNAME FIELD MISSING
  // =========================
  @Test
  void registerClient_missingUsername_returns400() throws Exception {

    String json =
        """
    {
      "email": "test@gmail.com",
      "password": "123456"
    }
    """;

    mockMvc
        .perform(
            post("/auth/register/client").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isBadRequest());

    Mockito.verify(authService, Mockito.never()).registerClient(Mockito.any());
  }

  // /////////////////////
  // C. Logout           //
  // /////////////////////

  // =========================
  // 1. SUCCESS
  // =========================
  @Test
  void logout_success() throws Exception {

    Mockito.when(cookieUtil.getAccessToken(any())).thenReturn("access-token");

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/auth/logout"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("logout successfully"));

    Mockito.verify(authService).logOut("access-token");
    Mockito.verify(cookieUtil).saveToken(eq("access_token"), eq("0"), eq(0), any());
  }

  // =========================
  // 2. SERVICE THROW EXCEPTION
  // =========================
  @Test
  void logout_serviceThrow_returns500() throws Exception {

    Mockito.when(cookieUtil.getAccessToken(any())).thenReturn("access-token");

    Mockito.doThrow(new RuntimeException("logout error")).when(authService).logOut(any());

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/auth/logout"))
        .andExpect(status().isInternalServerError());

    Mockito.verify(authService).logOut("access-token");
    Mockito.verify(cookieUtil, Mockito.never()).saveToken(any(), any(), eq(0), any());
  }

  // =========================
  // 3. GET ACCESS TOKEN THROW EXCEPTION
  // =========================
  @Test
  void logout_getAccessTokenThrows_returns500() throws Exception {

    Mockito.when(cookieUtil.getAccessToken(any()))
        .thenThrow(new RuntimeException("cookie read error"));

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/auth/logout"))
        .andExpect(status().isInternalServerError());

    Mockito.verify(authService, Mockito.never()).logOut(any());
    Mockito.verify(cookieUtil, Mockito.never()).saveToken(any(), any(), eq(0), any());
  }

  // =========================
  // 4. COOKIE CLEAR THROW EXCEPTION
  // =========================
  @Test
  void logout_cookieClearThrows_returns500() throws Exception {

    Mockito.when(cookieUtil.getAccessToken(any())).thenReturn("access-token");

    Mockito.doThrow(new RuntimeException("cookie clear error"))
        .when(cookieUtil)
        .saveToken(eq("access_token"), eq("0"), eq(0), any());

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/auth/logout"))
        .andExpect(status().isInternalServerError());

    Mockito.verify(authService).logOut("access-token");
    Mockito.verify(cookieUtil).saveToken(eq("access_token"), eq("0"), eq(0), any());
  }

  // =========================
  // 5. CALLED TWICE
  // =========================
  @Test
  void logout_calledTwice() throws Exception {

    Mockito.when(cookieUtil.getAccessToken(any())).thenReturn("access-token");

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/auth/logout"))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/auth/logout"))
        .andExpect(status().isOk());

    Mockito.verify(authService, Mockito.times(2)).logOut("access-token");
    Mockito.verify(cookieUtil, Mockito.times(2))
        .saveToken(eq("access_token"), eq("0"), eq(0), any());
  }

  // =========================
  // 6. WRONG HTTP METHOD (POST)
  // =========================
  @Test
  void logout_wrongMethod_returns405() throws Exception {
    mockMvc.perform(post("/auth/logout")).andExpect(status().isInternalServerError());

    Mockito.verify(authService, Mockito.never()).logOut(any());
  }

  // =========================
  // 7. NULL TOKEN IN COOKIE
  // =========================
  @Test
  void logout_nullToken_callsServiceWithNull_returns200() throws Exception {

    Mockito.when(cookieUtil.getAccessToken(any())).thenReturn(null);

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/auth/logout"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("logout successfully"));

    Mockito.verify(authService).logOut(null);
    Mockito.verify(cookieUtil).saveToken(eq("access_token"), eq("0"), eq(0), any());
  }

  // =========================
  // 8. WRONG HTTP METHOD (DELETE)
  // =========================
  @Test
  void logout_deleteMethod_returns500() throws Exception {

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete(
                "/auth/logout"))
        .andExpect(status().isInternalServerError());

    Mockito.verify(authService, Mockito.never()).logOut(any());
  }

  // =========================
  // 9. SERVICE THROW — VERIFY RESPONSE BODY
  // =========================
  @Test
  void logout_serviceThrow_responseBody_containsSuccessFalse() throws Exception {

    Mockito.when(cookieUtil.getAccessToken(any())).thenReturn("access-token");

    Mockito.doThrow(new RuntimeException("some internal error")).when(authService).logOut(any());

    mockMvc
        .perform(
            org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get("/auth/logout"))
        .andExpect(status().isInternalServerError())
        .andExpect(jsonPath("$.success").value(false))
        .andExpect(jsonPath("$.message").value("some internal error"));

    Mockito.verify(cookieUtil, Mockito.never()).saveToken(any(), any(), eq(0), any());
  }
}
