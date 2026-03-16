package com.e_cormerce.shoppe.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "phone_numbers",
    indexes = {@Index(name = "idx_user", columnList = "user_id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneNumber {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(name = "val", unique = true)
  String val;

  @Column(name = "is_default")
  Boolean isDefault = false;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @ManyToOne
  @JoinColumn(name = "user_id")
  User user;
}
