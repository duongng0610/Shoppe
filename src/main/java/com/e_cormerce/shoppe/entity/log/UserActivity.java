package com.e_cormerce.shoppe.entity.log;


import com.e_cormerce.shoppe.enums.user.ActionType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(
        name = "user_activities",
        indexes = {@Index(name = "idx_user", columnList = "user_id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE notifications SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class UserActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(name = "user_id", nullable = false)
    String userId;

    @Column(name = "username", nullable = false)
    String username;

    @Enumerated(EnumType.STRING)
    @Column(name = "action", nullable = false)
    ActionType action;

    @Column(name = "target_id")
    String targetId;

    @Column(name = "target_type")
    String targetType;


    @Column(name = "user_agent")
    String userAgent;

    @Column(name = "metadata", columnDefinition = "json")
    String metadata;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false,
            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdAt;
}