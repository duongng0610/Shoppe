package com.e_cormerce.shoppe.entity.conversation;

import com.e_cormerce.shoppe.entity.user.User;
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
@Table(
        name = "conversation_messages",
        indexes = {
                @Index(name = "idx_msg_conversation_id", columnList = "conversation_id"),
                @Index(name = "idx_msg_created_at", columnList = "created_at")
        })
@SQLDelete(sql = "UPDATE conversation_messages SET deleted=true WHERE id=?")
@Where(clause = "deleted = false")
public class ConversationMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "TEXT")
    String content;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    User sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_id", nullable = false)
    @JsonIgnore
    Conversation conversation;

    @OneToMany(
            mappedBy = "message",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.EAGER)
    List<MessageMedia> medias;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, nullable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    LocalDateTime updatedAt;

    @PreUpdate
    void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addMessageMedia(MessageMedia media) {
        if (medias == null) {
            medias = new ArrayList<>();
        }
        medias.add(media);
        media.setMessage(this);
    }
}
