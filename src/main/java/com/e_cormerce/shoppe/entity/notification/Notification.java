package com.e_cormerce.shoppe.entity.notification;

import com.e_cormerce.shoppe.entity.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

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

  @Column(name = "created_at", nullable = false)
  @CreationTimestamp
  LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JsonIgnore
  @JoinColumn(name = "user_id", nullable = false)
  User user;

  @Column(name = "read_at")
  LocalDateTime readAt;

  @Column(name = "is_read", columnDefinition = "boolean default false")
  Boolean isRead;

  @Column(name = "type", nullable = false)
  String type;

  @Column(name = "target_id")
  String targetId;

  @Column(name = "image")
  String image;
}
