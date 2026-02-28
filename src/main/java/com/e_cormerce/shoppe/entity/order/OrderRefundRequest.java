package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.OrderRefundStatus;
import com.e_cormerce.shoppe.enums.OrderRefundType;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "order_refund_requests",
        indexes = {@Index(name = "idx_seller", columnList = "seller_id")}
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRefundRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false, columnDefinition = "TEXT")
    String reason;

    @Column(nullable = false)
    BigDecimal refundAmount;

    @Column(nullable = false)
    LocalDateTime created_at;

    LocalDateTime resolved_at;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM(CLIENT_REFUND, SHIPPER_REFUND)", nullable = false)
    OrderRefundType refund_type;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('PENDING', 'REJECTED', 'APPROVED')" ,nullable = false)
    OrderRefundStatus status;

    @ManyToOne
    @JoinColumn(name = "requester_id")
    User requester;

    @ManyToOne
    @JoinColumn(name = "resolver_id")
    User seller;

    @ManyToOne
    @JoinColumn(name = "order_id")
    Order order;
}
