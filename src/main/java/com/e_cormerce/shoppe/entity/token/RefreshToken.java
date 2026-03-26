package com.e_cormerce.shoppe.entity.token;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "refresh_tokens")
@SQLDelete(sql = "UPDATE refresh_tokens SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class RefreshToken {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(nullable = false, unique = true)
  String val;

  @Column(nullable = false)
  @CreationTimestamp
  LocalDate date;

  @Column(name = "is_revoked", columnDefinition = "boolean default false")
  boolean isRevoked;
}
