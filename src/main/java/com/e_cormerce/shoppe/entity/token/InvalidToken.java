package com.e_cormerce.shoppe.entity.token;

import jakarta.persistence.*;
import java.time.LocalDate;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
    name = "invalid_tokens",
    indexes = {@Index(name = "idx_token", columnList = "val")})
@SQLDelete(sql = "UPDATE invalid_tokens SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class InvalidToken {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(nullable = false, unique = true)
  String val;

  @Column(name = "invalid_date", nullable = false)
  LocalDate invalidDate;
}
