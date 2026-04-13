package com.e_cormerce.shoppe.entity.user;

import jakarta.persistence.*;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
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

  @Column(nullable = false, unique = true)
  String val;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  // owner side
  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(
      name = "role_permission",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"),
      indexes = {@Index(name = "idx_role", columnList = "role_id")})
  Set<Permission> permissions;
}
