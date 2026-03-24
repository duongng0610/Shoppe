package com.e_cormerce.shoppe.entity.user;

import com.e_cormerce.shoppe.entity.transaction.Transaction;
import com.e_cormerce.shoppe.enums.user.UserStatus;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "users",
    indexes = {
      @Index(name = "idx_users_username", columnList = "username"),
      @Index(name = "idx_users_role", columnList = "role_id")
    })
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(nullable = false, unique = true)
  String username;

  String avatar;
  LocalDate birth;

  @Column(name = "created_at")
  LocalDateTime createdAt;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_status") // , nullable = false)
  UserStatus userStatus;

  // owner side
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id")
  Account account;

  // owner side
  @ManyToOne(
      fetch = FetchType.LAZY,
      cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinColumn(name = "address_id")
  Address address;

  // owner side
  @ManyToOne
  @JoinColumn(name = "role_id", nullable = false)
  Role role;

  // inverse side
  @OneToMany(
      mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  Set<PhoneNumber> phoneNumbers;

  // inverse side
  @OneToMany(
      mappedBy = "user",
      cascade = CascadeType.ALL,
      orphanRemoval = true,
      fetch = FetchType.LAZY)
  Set<Transaction> transactions;
}
