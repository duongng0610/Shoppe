package com.e_cormerce.shoppe.entity.conversation;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "conversations")
@SQLDelete(sql = "UPDATE conversations SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @Column(name = "last_content", columnDefinition = "TEXT", nullable = false)
    String lastContent;

    @Column(name = "last_content_at", nullable = false)
    LocalDateTime lastContentAt;

    // có thể là người gửi / từ hệ thống ,ví dụ :tạo cuộc trò chuyện mới thì ko cần senderId.
    @Column(name = "last_sender_id", nullable = true)
    String lastSenderId;

    @JsonIgnore
    @OneToMany(
            mappedBy = "conversation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    List<ConversationMember> members;

    @JsonIgnore
    @OneToMany(
            mappedBy = "conversation",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY)
    List<ConversationMessage> messages;

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addMember(ConversationMember conversationMember) {
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(conversationMember);
        conversationMember.setConversation(this);
    }

    public void addMessage(ConversationMessage message) {
        if (messages == null) {
            messages = new ArrayList<>();
        }
        messages.add(message);
        message.setConversation(this);
    }
}
