package com.e_cormerce.shoppe.entity.user;

import com.e_cormerce.shoppe.validator.StrongPassword;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.antlr.v4.runtime.misc.NotNull;

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
public class Account {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(unique = true, nullable = false)
  String email;

  @Column(nullable = false)
  @StrongPassword
  String password;

  @Column(name = "created_at", nullable = false)
  @NotNull
  Date created_at;

  @Column(name = "updated_at")
  Date updatedAt;

  @Column(name = "last_login_at")
  @NotNull
  LocalDateTime lastLoginAt;

  @Column(name = "last_active_at")
  @NotNull
  LocalDateTime lastActiveAt;

  @Column(name = "is_active")
  @NotNull
  boolean isActive;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  // inverse side
  @OneToOne(mappedBy = "account", cascade = CascadeType.ALL)
  User user;
}
