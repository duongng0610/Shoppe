package com.e_cormerce.shoppe.service.notification;

import com.e_cormerce.shoppe.entity.notification.Notification;
import com.e_cormerce.shoppe.enums.notification.NotificationType;
import com.e_cormerce.shoppe.repository.notification.NotificationRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationService {
  UserRepository userRepository;
  NotificationRepository notificationRepository;
  AuthService authService;

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void createNotification(
      String userId,
      NotificationType notificationType,
      String targetId,
      String title,
      String content,
      String image) {

    Notification notification =
        Notification.builder()
            .userId(userId)
            .type(notificationType)
            .title(title)
            .content(content)
            .targetId(targetId)
            .isRead(false)
            .image(image)
            .build();

    notificationRepository.save(notification);
  }

  public long countUnReadNotifications() {

    return notificationRepository.countUnReadNotificationsByUser(authService.getUserId());
  }

  public long countUnReadMessageNotifications() {
    return notificationRepository.countUnReadMessagesNotificationsByUser(authService.getUserId());
  }

  // 🔸 Lấy danh sách notification (đã read + chưa read vì query của bạn đang lấy all)
  public List<Notification> getNotifications(int limit, int offset) {

    return notificationRepository.getNotificationsByUser(authService.getUserId(), limit, offset);
  }

  // 🔸 Mark as read
  @Transactional
  public void readNotifications(List<String> ids) {
    notificationRepository.markAsReadNative(ids);
    return;
  }
}
