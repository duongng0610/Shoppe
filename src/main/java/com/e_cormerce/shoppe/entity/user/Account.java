package com.e_cormerce.shoppe.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Table(
        name = "accounts",
        indexes = {@Index(name = "idx_email", columnList = "email")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE accounts SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, nullable = false)
    String email;

    @Column(nullable = false)
    String password;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    Date updatedAt;

    @Column(name = "last_login_at")
    LocalDateTime lastLoginAt;

    @Column(name = "last_active_at")
    LocalDateTime lastActiveAt;

    @Column(name = "is_active")
    Boolean isActive;

    @Column(name = "is_banned", nullable = false, columnDefinition = "boolean default false")
    Boolean isBanned;

    @Column(columnDefinition = "boolean default false")
    Boolean deleted;

    // owner side
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false)
    Role role;

    // inverse side
    @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
    User user;
}
