package com.e_cormerce.shoppe.entity;

import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.TargetTypeTransaction;
import com.e_cormerce.shoppe.enums.TransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.antlr.v4.runtime.misc.NotNull;

import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "transactions", indexes = {@Index(name = "idx_user", columnList = "user_id"),

})
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @NotNull
    String message;

    TransactionStatus status;

    @Column(name = "created_at", nullable = false)
    @NotNull
    Date created_at;

    @Column(name = "target_id", nullable = true)
    @NotNull
    String target_id;

    @Column
    @NotNull
    TargetTypeTransaction target_type_transaction;

}
