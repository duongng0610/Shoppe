package com.e_cormerce.shoppe.entity.conversation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
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

  @Column(name = "url", nullable = false)
  String url;

  @Column(name = "image_id", nullable = false)
  String imageId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "message_id", nullable = false)
  @JsonIgnore
  ConversationMessage message;

  @Column(columnDefinition = "boolean default false", nullable = false)
  boolean deleted;

  @CreationTimestamp
  @Column(
      name = "created_at",
      updatable = false,
      nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  LocalDateTime updatedAt;
}
