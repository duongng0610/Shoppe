package com.e_cormerce.shoppe.controller.client.ControllerTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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
class ClientControllerTestPassword {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    AccountService accountService;

    private final String VALID_BODY = """
            {
              "oldPassword": "123456",
              "newPassword": "654321"
            }
            """;

    // =========================
    // 1. SUCCESS
    // =========================
    @Test
    void changePassword_success() throws Exception {

        mockMvc.perform(patch("/user/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.message").value("change password successfully"));

        Mockito.verify(accountService).changePassword(Mockito.any());
    }

    // =========================
    // 2. SERVICE THROW (wrong old password)
    // =========================
    @Test
    void changePassword_wrongOldPassword_returns400() throws Exception {

        Mockito.doThrow(new AppException(ErrorCode.INCORRECT_PASSWORD))
                .when(accountService).changePassword(Mockito.any());

        mockMvc.perform(patch("/user/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isBadRequest());

        Mockito.verify(accountService).changePassword(Mockito.any());
    }

    // =========================
    // 3. MISSING BODY
    // =========================
    @Test
    void changePassword_missingBody_returns400() throws Exception {

        mockMvc.perform(patch("/user/account/password")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());

        Mockito.verify(accountService, Mockito.never())
                .changePassword(Mockito.any());
    }

    // =========================
    // 4. INVALID JSON
    // =========================
    @Test
    void changePassword_invalidJson_returns400() throws Exception {

        String invalid = "{ invalid json }";

        mockMvc.perform(patch("/user/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalid))
                .andExpect(status().isBadRequest());

        Mockito.verify(accountService, Mockito.never())
                .changePassword(Mockito.any());
    }

    // =========================
    // 5. BLANK FIELDS
    // =========================
    @Test
    void changePassword_blankFields_returns400() throws Exception {

        String body = """
                {
                  "oldPassword": "",
                  "newPassword": ""
                }
                """;

        mockMvc.perform(patch("/user/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(accountService, Mockito.never())
                .changePassword(Mockito.any());
    }

    // =========================
    // 6. NEW PASSWORD TOO SHORT
    // =========================
    @Test
    void changePassword_shortNewPassword_returns400() throws Exception {

        String body = """
                {
                  "oldPassword": "123456",
                  "newPassword": "1"
                }
                """;

        mockMvc.perform(patch("/user/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(accountService, Mockito.never())
                .changePassword(Mockito.any());
    }

    // =========================
    // 7. EMPTY BODY
    // =========================
    @Test
    void changePassword_emptyBody_returns400() throws Exception {

        mockMvc.perform(patch("/user/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(""))
                .andExpect(status().isBadRequest());

        Mockito.verify(accountService, Mockito.never())
                .changePassword(Mockito.any());
    }

    // =========================
    // 8. NULL PASSWORD FIELDS
    // =========================
    @Test
    void changePassword_nullFields_returns400() throws Exception {

        String body = """
                {
                  "oldPassword": null,
                  "newPassword": null
                }
                """;

        mockMvc.perform(patch("/user/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest());

        Mockito.verify(accountService, Mockito.never())
                .changePassword(Mockito.any());
    }

    // =========================
    // 9. SERVICE THROW RUNTIME EXCEPTION
    // =========================
    @Test
    void changePassword_serviceThrowRuntime_returns500() throws Exception {

        Mockito.doThrow(new RuntimeException("server error"))
                .when(accountService)
                .changePassword(Mockito.any());

        mockMvc.perform(patch("/user/account/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(VALID_BODY))
                .andExpect(status().isInternalServerError());

        Mockito.verify(accountService)
                .changePassword(Mockito.any());
    }

    // =========================
    // 10. INVALID CONTENT TYPE
    // =========================
    @Test
    void changePassword_invalidContentType_returns415() throws Exception {

        mockMvc.perform(patch("/user/account/password")
                        .contentType(MediaType.TEXT_PLAIN)
                        .content(VALID_BODY))
                .andExpect(status().isUnsupportedMediaType());

        Mockito.verify(accountService, Mockito.never())
                .changePassword(Mockito.any());
    }
}