package com.e_cormerce.shoppe.entity.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(name = "order_refund_request_medias",
        indexes = {@Index(name = "idx_order_refund_request", columnList = "order_refund_request_id")}
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRefundRequestMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(nullable = false)
    String url;

    @ManyToOne
    @JoinColumn(name = "order_refund_request_id")
    OrderRefundRequest orderRefundRequest;
}
