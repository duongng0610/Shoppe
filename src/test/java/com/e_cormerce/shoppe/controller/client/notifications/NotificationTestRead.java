package com.e_cormerce.shoppe.controller.client.notifications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.service.notification.NotificationService;
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
class NotificationControllerTestRead {

  @Autowired MockMvc mockMvc;

  @MockitoBean NotificationService notificationService;

  // =========================
  // 1. SUCCESS
  // =========================
  @Test
  void readNotifications_success() throws Exception {

    String json =
        """
                {
                  "ids": ["1", "2"]
                }
                """;

    mockMvc
        .perform(
            put("/user/notifications/read").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Doc thong bao thanh cong"));

    Mockito.verify(notificationService).readNotifications(Mockito.anyList());
  }

  // =========================
  // 2. EMPTY IDS
  // =========================
  @Test
  void readNotifications_emptyIds_returns200() throws Exception {

    String json =
        """
                {
                  "ids": []
                }
                """;

    mockMvc
        .perform(
            put("/user/notifications/read").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());

    Mockito.verify(notificationService).readNotifications(Mockito.anyList());
  }

  // =========================
  // 3. MISSING BODY
  // =========================
  @Test
  void readNotifications_missingBody_returns400() throws Exception {

    mockMvc
        .perform(put("/user/notifications/read").contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest());

    Mockito.verify(notificationService, Mockito.never()).readNotifications(Mockito.anyList());
  }

  // =========================
  // 4. INVALID JSON
  // =========================
  @Test
  void readNotifications_invalidJson_returns400() throws Exception {

    String invalidJson = "{ invalid json }";

    mockMvc
        .perform(
            put("/user/notifications/read")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
        .andExpect(status().isBadRequest());

    Mockito.verify(notificationService, Mockito.never()).readNotifications(Mockito.anyList());
  }

  // =========================
  // 5. SERVICE THROW EXCEPTION
  // =========================
  @Test
  void readNotifications_serviceThrow_returns500() throws Exception {

    String json =
        """
                {
                  "ids": ["1"]
                }
                """;

    Mockito.doThrow(new RuntimeException("error"))
        .when(notificationService)
        .readNotifications(Mockito.anyList());

    mockMvc
        .perform(
            put("/user/notifications/read").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isInternalServerError());

    Mockito.verify(notificationService).readNotifications(Mockito.anyList());
  }

  // =========================
  // 6. IDS NULL
  // =========================
  @Test
  void readNotifications_idsNull_returns200() throws Exception {

    String json =
        """
                {
                  "ids": null
                }
                """;

    mockMvc
        .perform(
            put("/user/notifications/read").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());

    Mockito.verify(notificationService).readNotifications(Mockito.anyList());
  }

  // =========================
  // 7. MULTIPLE IDS
  // =========================
  @Test
  void readNotifications_multipleIds_returns200() throws Exception {

    String json =
        """
                {
                  "ids": ["1", "2", "3", "4", "5"]
                }
                """;

    mockMvc
        .perform(
            put("/user/notifications/read").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.message").value("Doc thong bao thanh cong"));

    Mockito.verify(notificationService).readNotifications(Mockito.anyList());
  }

  // =========================
  // 8. EMPTY JSON OBJECT
  // =========================
  @Test
  void readNotifications_emptyJson_returns200() throws Exception {

    String json = """
                {
                }
                """;

    mockMvc
        .perform(
            put("/user/notifications/read").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());

    Mockito.verify(notificationService).readNotifications(Mockito.anyList());
  }

  // =========================
  // 9. WRONG FIELD NAME
  // =========================
  @Test
  void readNotifications_wrongFieldName_returns200() throws Exception {

    String json =
        """
                {
                  "idList": ["1", "2"]
                }
                """;

    mockMvc
        .perform(
            put("/user/notifications/read").contentType(MediaType.APPLICATION_JSON).content(json))
        .andExpect(status().isOk());

    Mockito.verify(notificationService).readNotifications(Mockito.anyList());
  }

  // =========================
  // 10. CONTENT TYPE NOT JSON
  // =========================
  @Test
  void readNotifications_invalidContentType_returns415() throws Exception {

    String json =
        """
                {
                  "ids": ["1"]
                }
                """;

    mockMvc
        .perform(put("/user/notifications/read").contentType(MediaType.TEXT_PLAIN).content(json))
        .andExpect(status().isUnsupportedMediaType());

    Mockito.verify(notificationService, Mockito.never()).readNotifications(Mockito.anyList());
  }
}
