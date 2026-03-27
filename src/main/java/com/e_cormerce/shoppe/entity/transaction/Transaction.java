package com.e_cormerce.shoppe.entity.transaction;

import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.transaction.TargetTypeTransaction;
import com.e_cormerce.shoppe.enums.transaction.TransactionStatus;
import jakarta.persistence.*;
import java.util.Date;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.antlr.v4.runtime.misc.NotNull;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
    name = "transactions",
    indexes = {
      @Index(name = "idx_user", columnList = "user_id"),
    })
@SQLDelete(sql = "UPDATE transactions SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class Transaction {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @ManyToOne
  @JoinColumn(name = "user_id", nullable = false)
  User user;

  @NotNull String message;

  TransactionStatus status;

  @CreationTimestamp
  @Column(name = "created_at", nullable = false)
  @NotNull
  Date createdAt;

  @Column(name = "target_id", nullable = false)
  @NotNull
  String targetId;

  @Column @NotNull TargetTypeTransaction target_type_transaction;
}
