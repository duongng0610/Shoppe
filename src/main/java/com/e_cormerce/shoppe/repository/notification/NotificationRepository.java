package com.e_cormerce.shoppe.repository.notification;

import com.e_cormerce.shoppe.entity.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, String> {


    // Đếm số notification chưa đọc
    @Query(value = """
    SELECT COUNT(*) 
    FROM notifications 
    WHERE user_id = :userId 
      AND is_read = false
""", nativeQuery = true)
    long countUnReadNotificationsByUser(@Param("userId") String userId);

    // Lấy danh sách notification đã đọc
    @Query(value = """
    SELECT * 
    FROM notifications 
    WHERE user_id = :userId 
    ORDER BY created_at DESC
    LIMIT :limit OFFSET :offset
""", nativeQuery = true)
    List<Notification> getNotificationsByUser(@Param("userId") String userId,
                                              @Param("limit") int limit,
                                              @Param("offset") int offset);

    @Modifying
    @Query(value = """
    UPDATE notifications 
    SET is_read = true, read_at = NOW()
    WHERE id IN (:ids)
""", nativeQuery = true)
    int markAsReadNative(@Param("ids") List<String> ids);

}
