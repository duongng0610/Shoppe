package com.e_cormerce.shoppe.controller.client.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.dto.response.account.UserProfileResponse;
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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@FieldDefaults(level = AccessLevel.PRIVATE)
@ActiveProfiles("test")
class AccountControllerTestGetProfile {

  @Autowired MockMvc mockMvc;

  @MockitoBean AccountService accountService;

  // =========================
  // 1. SUCCESS
  // =========================
  @Test
  void getProfile_success() throws Exception {

    UserProfileResponse response = UserProfileResponse.builder().username("ducnguyen").build();

    Mockito.when(accountService.getUserProfile()).thenReturn(response);

    mockMvc
        .perform(get("/user/account/profile"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("profile successfully"))
        .andExpect(jsonPath("$.data.username").value("ducnguyen"));

    Mockito.verify(accountService).getUserProfile();
  }

  // =========================
  // 2. USER NOT FOUND
  // =========================
  @Test
  void getProfile_userNotFound_returns400() throws Exception {

    Mockito.doThrow(new AppException(ErrorCode.NOT_EXIST_USER))
        .when(accountService)
        .getUserProfile();

    mockMvc.perform(get("/user/account/profile")).andExpect(status().isBadRequest());

    Mockito.verify(accountService).getUserProfile();
  }

  // =========================
  // 3. SERVICE THROW RUNTIME EXCEPTION
  // =========================
  @Test
  void getProfile_serviceThrow_returns500() throws Exception {

    Mockito.doThrow(new RuntimeException("server error")).when(accountService).getUserProfile();

    mockMvc.perform(get("/user/account/profile")).andExpect(status().isInternalServerError());

    Mockito.verify(accountService).getUserProfile();
  }

  // =========================
  // 4. RESPONSE DATA NULL
  // =========================
  @Test
  void getProfile_nullResponse_returns200() throws Exception {

    Mockito.when(accountService.getUserProfile()).thenReturn(null);

    mockMvc
        .perform(get("/user/account/profile"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("profile successfully"));

    Mockito.verify(accountService).getUserProfile();
  }

  // =========================
  // 5. MULTIPLE CALL VERIFY
  // =========================
  @Test
  void getProfile_calledTwice() throws Exception {

    UserProfileResponse response =
        UserProfileResponse.builder().username("ducnguyen").avatar("avatar.png").build();

    Mockito.when(accountService.getUserProfile()).thenReturn(response);

    mockMvc.perform(get("/user/account/profile")).andExpect(status().isOk());

    mockMvc.perform(get("/user/account/profile")).andExpect(status().isOk());

    Mockito.verify(accountService, Mockito.times(2)).getUserProfile();
  }

  // =========================
  // 6. RESPONSE CONTAINS SUCCESS MESSAGE
  // =========================
  @Test
  void getProfile_responseContainsMessage() throws Exception {

    UserProfileResponse response = UserProfileResponse.builder().username("testuser").build();

    Mockito.when(accountService.getUserProfile()).thenReturn(response);

    mockMvc
        .perform(get("/user/account/profile"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("profile successfully"))
        .andExpect(jsonPath("$.data.username").value("testuser"));

    Mockito.verify(accountService).getUserProfile();
  }

  // =========================
  // 7. RESPONSE WITH AVATAR
  // =========================
  @Test
  void getProfile_withAvatar_returns200() throws Exception {

    UserProfileResponse response =
        UserProfileResponse.builder().username("ducnguyen").avatar("avatar.png").build();

    Mockito.when(accountService.getUserProfile()).thenReturn(response);

    mockMvc
        .perform(get("/user/account/profile"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.username").value("ducnguyen"))
        .andExpect(jsonPath("$.data.avatar").value("avatar.png"));

    Mockito.verify(accountService).getUserProfile();
  }

  // =========================
  // 8. RESPONSE USERNAME NULL
  // =========================
  @Test
  void getProfile_usernameNull_returns200() throws Exception {

    UserProfileResponse response =
        UserProfileResponse.builder().username(null).avatar("avatar.png").build();

    Mockito.when(accountService.getUserProfile()).thenReturn(response);

    mockMvc
        .perform(get("/user/account/profile"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.avatar").value("avatar.png"));

    Mockito.verify(accountService).getUserProfile();
  }

  // =========================
  // 9. VERIFY SUCCESS FIELD
  // =========================
  @Test
  void getProfile_verifySuccessField_returnsTrue() throws Exception {

    UserProfileResponse response = UserProfileResponse.builder().username("ducnguyen").build();

    Mockito.when(accountService.getUserProfile()).thenReturn(response);

    mockMvc
        .perform(get("/user/account/profile"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true));

    Mockito.verify(accountService).getUserProfile();
  }

  // =========================
  // 10. RESPONSE EMPTY OBJECT
  // =========================
  @Test
  void getProfile_emptyResponseObject_returns200() throws Exception {

    UserProfileResponse response = UserProfileResponse.builder().build();

    Mockito.when(accountService.getUserProfile()).thenReturn(response);

    mockMvc
        .perform(get("/user/account/profile"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("profile successfully"));

    Mockito.verify(accountService).getUserProfile();
  }
}
