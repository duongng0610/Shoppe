package com.e_cormerce.shoppe.entity.conversation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
    name = "message_medias",
    indexes = {@Index(name = "idx_mm_message_id", columnList = "message_id")})
@SQLDelete(sql = "UPDATE message_medias SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class MessageMedia {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  String url;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "message_id", nullable = false)
  @JsonIgnore
  ConversationMessage message;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  LocalDateTime createdAt;

  @Column(name = "updated_at")
  LocalDateTime updatedAt;

  @PreUpdate
  void onUpdate() {
    updatedAt = LocalDateTime.now();
  }
}
