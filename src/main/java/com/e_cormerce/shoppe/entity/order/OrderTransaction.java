package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.OrderTransactionStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)

public class OrderTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(precision = 15, scale = 2, nullable = false)
    BigDecimal amount;

    @Column(nullable = false)
    LocalDateTime created_at;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderTransactionStatus status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false)
    Order order;

}
