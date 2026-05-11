package com.e_cormerce.shoppe.controller.client.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.dto.response.account.ChangeUserProfileResponse;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.service.account.AccountService;
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
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
@ActiveProfiles("test")
class AccountControllerTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean AccountService accountService;

  private final String VALID_BODY =
      """
                {
                  "username": "ducnguyen",
                  "thumbnailUrl": "avatar.png"
                }
                """;

  // =========================
  // 1. SUCCESS
  // =========================
  @Test
  void updateProfile_success() throws Exception {

    ChangeUserProfileResponse response =
        ChangeUserProfileResponse.builder().username("ducnguyen").avatar("avatar.png").build();

    Mockito.when(accountService.updateProfile(Mockito.any())).thenReturn(response);

    mockMvc
        .perform(
            put("/user/account/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BODY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("update profile successfully"))
        .andExpect(jsonPath("$.data.username").value("ducnguyen"));

    Mockito.verify(accountService).updateProfile(Mockito.any());
  }

  // =========================
  // 2. MISSING BODY
  // =========================
  @Test
  void updateProfile_missingBody_returns400() throws Exception {

    mockMvc
        .perform(put("/user/account/profile").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    Mockito.verify(accountService, Mockito.never()).updateProfile(Mockito.any());
  }

  // =========================
  // 3. EMPTY BODY
  // =========================
  @Test
  void updateProfile_emptyBody_returns400() throws Exception {

    mockMvc
        .perform(put("/user/account/profile").contentType(MediaType.APPLICATION_JSON).content(""))
        .andExpect(status().isBadRequest());

    Mockito.verify(accountService, Mockito.never()).updateProfile(Mockito.any());
  }

  // =========================
  // 4. INVALID JSON
  // =========================
  @Test
  void updateProfile_invalidJson_returns400() throws Exception {

    String invalidJson = "{ invalid json }";

    mockMvc
        .perform(
            put("/user/account/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
        .andExpect(status().isBadRequest());

    Mockito.verify(accountService, Mockito.never()).updateProfile(Mockito.any());
  }

  // =========================
  // 5. SERVICE THROW EXCEPTION
  // =========================
  @Test
  void updateProfile_serviceThrow_returns400() throws Exception {

    Mockito.doThrow(new AppException(ErrorCode.INVALID_USERNAME))
        .when(accountService)
        .updateProfile(Mockito.any());

    mockMvc
        .perform(
            put("/user/account/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BODY))
        .andExpect(status().isBadRequest());

    Mockito.verify(accountService).updateProfile(Mockito.any());
  }

  // =========================
  // 6. MULTIPLE CALL VERIFY
  // =========================
  @Test
  void updateProfile_calledTwice() throws Exception {

    ChangeUserProfileResponse response =
        ChangeUserProfileResponse.builder().username("ducnguyen").avatar("avatar.png").build();

    Mockito.when(accountService.updateProfile(Mockito.any())).thenReturn(response);

    mockMvc
        .perform(
            put("/user/account/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BODY))
        .andExpect(status().isOk());

    mockMvc
        .perform(
            put("/user/account/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BODY))
        .andExpect(status().isOk());

    Mockito.verify(accountService, Mockito.times(2)).updateProfile(Mockito.any());
  }

  // =========================
  // 7. BLANK USERNAME
  // =========================
  @Test
  void updateProfile_blankUsername_returns400() throws Exception {

    String body =
        """
                {
                  "username": "",
                  "thumbnailUrl": "avatar.png"
                }
                """;

    mockMvc
        .perform(put("/user/account/profile").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(accountService, Mockito.never()).updateProfile(Mockito.any());
  }

  // =========================
  // 8. NULL USERNAME
  // =========================
  @Test
  void updateProfile_nullUsername_returns400() throws Exception {

    String body =
        """
                {
                  "username": null,
                  "thumbnailUrl": "avatar.png"
                }
                """;

    mockMvc
        .perform(put("/user/account/profile").contentType(MediaType.APPLICATION_JSON).content(body))
        .andExpect(status().isBadRequest());

    Mockito.verify(accountService, Mockito.never()).updateProfile(Mockito.any());
  }

  // =========================
  // 9. INVALID CONTENT TYPE
  // =========================
  @Test
  void updateProfile_invalidContentType_returns415() throws Exception {

    mockMvc
        .perform(put("/user/account/profile").contentType(MediaType.TEXT_PLAIN).content(VALID_BODY))
        .andExpect(status().isUnsupportedMediaType());

    Mockito.verify(accountService, Mockito.never()).updateProfile(Mockito.any());
  }

  // =========================
  // 10. RESPONSE DATA NULL
  // =========================
  @Test
  void updateProfile_responseDataNull_returns200() throws Exception {

    Mockito.when(accountService.updateProfile(Mockito.any())).thenReturn(null);

    mockMvc
        .perform(
            put("/user/account/profile")
                .contentType(MediaType.APPLICATION_JSON)
                .content(VALID_BODY))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("update profile successfully"));

    Mockito.verify(accountService).updateProfile(Mockito.any());
  }
}
