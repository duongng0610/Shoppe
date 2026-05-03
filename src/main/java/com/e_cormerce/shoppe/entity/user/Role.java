package com.e_cormerce.shoppe.entity.user;

import com.e_cormerce.shoppe.enums.user.RoleEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Entity
@Table(name = "role")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE role SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class Role {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, unique = true)
  RoleEnum val;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @CreationTimestamp
  @Column(
      name = "created_at",
      updatable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  Date updatedAt;

  // owner side
  @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
  @JoinTable(
      name = "role_permission",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"),
      indexes = {@Index(name = "idx_role", columnList = "role_id")})
  Set<Permission> permissions;

  public void addPermission(Permission permission) {
    if (this.permissions == null) {
      permissions = new HashSet<Permission>();
    }
    permissions.add(permission);
  }
}
