package com.e_cormerce.shoppe.entity.conversation;

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
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
    name = "conversation_members",
    indexes = {
      @Index(name = "idx_cm_member_id", columnList = "member_id"),
      @Index(name = "idx_cm_conversation_id", columnList = "conversation_id"),
      @Index(name = "idx_cm_conversation_member", columnList = "conversation_id, member_id")
    },
    uniqueConstraints = {@UniqueConstraint(columnNames = {"conversation_id", "member_id"})})
@SQLDelete(sql = "UPDATE conversation_members SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class ConversationMember {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "conversation_id", nullable = false)
  @JsonIgnore
  Conversation conversation;

  @ManyToOne
  @JoinColumn(name = "member_id", nullable = false)
  User member;

  // để check xem user này đã đọc chưa
  @Column(name = "last_read_at", nullable = true)
  LocalDateTime lastReadAt;

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
