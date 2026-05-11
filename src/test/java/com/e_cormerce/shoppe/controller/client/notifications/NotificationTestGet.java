package com.e_cormerce.shoppe.controller.client.notifications;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.entity.notification.Notification;
import com.e_cormerce.shoppe.enums.notification.NotificationType;
import com.e_cormerce.shoppe.service.notification.NotificationService;
import java.util.List;
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
class NotificationControllerTest {

  @Autowired MockMvc mockMvc;

  @MockitoBean NotificationService notificationService;

  // =========================
  // 1. SUCCESS
  // =========================
  @Test
  void getNotifications_success() throws Exception {

    Notification notification =
        Notification.builder()
            .id("1")
            .title("Thong bao")
            .content("Noi dung")
            .type(NotificationType.ORDER)
            .isRead(false)
            .build();

    Mockito.when(notificationService.getNotifications(10, 0)).thenReturn(List.of(notification));

    mockMvc
        .perform(get("/user/notifications"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].id").value("1"))
        .andExpect(jsonPath("$.data[0].title").value("Thong bao"))
        .andExpect(jsonPath("$.data[0].content").value("Noi dung"));

    Mockito.verify(notificationService).getNotifications(10, 0);
  }

  // =========================
  // 2. EMPTY LIST
  // =========================
  @Test
  void getNotifications_emptyList_returns200() throws Exception {

    Mockito.when(notificationService.getNotifications(10, 0)).thenReturn(List.of());

    mockMvc
        .perform(get("/user/notifications"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data").isArray());

    Mockito.verify(notificationService).getNotifications(10, 0);
  }

  // =========================
  // 3. CUSTOM LIMIT OFFSET
  // =========================
  @Test
  void getNotifications_customLimitOffset_returns200() throws Exception {

    Mockito.when(notificationService.getNotifications(5, 2)).thenReturn(List.of());

    mockMvc
        .perform(get("/user/notifications").param("limit", "5").param("offset", "2"))
        .andExpect(status().isOk());

    Mockito.verify(notificationService).getNotifications(5, 2);
  }

  // =========================
  // 4. SERVICE THROW EXCEPTION
  // =========================
  @Test
  void getNotifications_serviceThrow_returns500() throws Exception {

    Mockito.when(notificationService.getNotifications(10, 0))
        .thenThrow(new RuntimeException("error"));

    mockMvc.perform(get("/user/notifications")).andExpect(status().isInternalServerError());

    Mockito.verify(notificationService).getNotifications(10, 0);
  }

  // =========================
  // 5. INVALID LIMIT
  // =========================
  @Test
  void getNotifications_invalidLimit_returns400() throws Exception {

    mockMvc
        .perform(get("/user/notifications").param("limit", "abc"))
        .andExpect(status().isBadRequest());

    Mockito.verify(notificationService, Mockito.never())
        .getNotifications(Mockito.anyInt(), Mockito.anyInt());
  }

  // =========================
  // 6. INVALID OFFSET
  // =========================
  @Test
  void getNotifications_invalidOffset_returns400() throws Exception {

    mockMvc
        .perform(get("/user/notifications").param("offset", "abc"))
        .andExpect(status().isBadRequest());

    Mockito.verify(notificationService, Mockito.never())
        .getNotifications(Mockito.anyInt(), Mockito.anyInt());
  }

  // =========================
  // 7. NEGATIVE LIMIT
  // =========================
  @Test
  void getNotifications_negativeLimit_returns200() throws Exception {

    Mockito.when(notificationService.getNotifications(-1, 0)).thenReturn(List.of());

    mockMvc.perform(get("/user/notifications").param("limit", "-1")).andExpect(status().isOk());

    Mockito.verify(notificationService).getNotifications(-1, 0);
  }

  // =========================
  // 8. NEGATIVE OFFSET
  // =========================
  @Test
  void getNotifications_negativeOffset_returns200() throws Exception {

    Mockito.when(notificationService.getNotifications(10, -5)).thenReturn(List.of());

    mockMvc.perform(get("/user/notifications").param("offset", "-5")).andExpect(status().isOk());

    Mockito.verify(notificationService).getNotifications(10, -5);
  }

  // =========================
  // 9. LARGE LIMIT OFFSET
  // =========================
  @Test
  void getNotifications_largeLimitOffset_returns200() throws Exception {

    Mockito.when(notificationService.getNotifications(1000, 500)).thenReturn(List.of());

    mockMvc
        .perform(get("/user/notifications").param("limit", "1000").param("offset", "500"))
        .andExpect(status().isOk());

    Mockito.verify(notificationService).getNotifications(1000, 500);
  }

  // =========================
  // 10. MULTIPLE NOTIFICATIONS
  // =========================
  @Test
  void getNotifications_multipleNotifications_returns200() throws Exception {

    Notification n1 =
        Notification.builder()
            .id("1")
            .title("Thong bao 1")
            .content("Noi dung 1")
            .type(NotificationType.ORDER)
            .isRead(false)
            .build();

    Notification n2 =
        Notification.builder()
            .id("2")
            .title("Thong bao 2")
            .content("Noi dung 2")
            .type(NotificationType.MESSAGE)
            .isRead(true)
            .build();

    Mockito.when(notificationService.getNotifications(10, 0)).thenReturn(List.of(n1, n2));

    mockMvc
        .perform(get("/user/notifications"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data[0].id").value("1"))
        .andExpect(jsonPath("$.data[1].id").value("2"))
        .andExpect(jsonPath("$.data[1].title").value("Thong bao 2"));

    Mockito.verify(notificationService).getNotifications(10, 0);
  }
}
