package com.e_cormerce.shoppe.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
    name = "permissions",
    indexes = {@Index(name = "idx_val", columnList = "val")})
public class Permission {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false, unique = true)
  String val;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;
}
