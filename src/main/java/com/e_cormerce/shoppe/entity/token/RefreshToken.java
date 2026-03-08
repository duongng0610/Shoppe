package com.e_cormerce.shoppe.entity.token;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "refresh_tokens")
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false, unique = true)
  String val;

  @Column(nullable = false)
  LocalDate date;

  @Column(columnDefinition = "boolean default false")
  boolean isRevoked;
}
