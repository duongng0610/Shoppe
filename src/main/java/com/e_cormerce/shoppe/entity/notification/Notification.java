package com.e_cormerce.shoppe.entity.notification;

import com.e_cormerce.shoppe.enums.notification.NotificationType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "notifications",
        indexes = {@Index(name = "idx_user", columnList = "user_id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE notifications SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @Column(nullable = false)
    String title;

    @Column(nullable = false)
    String content;

    @Column(name = "created_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    @CreationTimestamp
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "user_id", nullable = false)
    String userId;


    @Column(name = "read_at")
    LocalDateTime readAt;

    @Column(name = "is_read", nullable = false, columnDefinition = "boolean default false")
    boolean isRead;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('ORDER', 'TRANSACTION', 'PRODUCT', 'ACCOUNT','SYSTEM')")
    NotificationType type;

    @Column(name = "target_id")
    String targetId;

    @Column(name = "image")
    String image;


}
