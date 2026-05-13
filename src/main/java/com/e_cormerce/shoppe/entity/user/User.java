package com.e_cormerce.shoppe.entity.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Past;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "users",
    indexes = {
      @Index(name = "idx_users_username", columnList = "username"),
    })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE users SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class User {
  @Id String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(nullable = false, unique = true)
  String username;

  @Column(name = "avatar")
  String avatar;

  @Past
  @Column(name = "dob")
  LocalDate dob;

  @Column(name = "phone_number")
  String phoneNumber;

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
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id")
  Account account;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "address_id")
  Address address;
}
