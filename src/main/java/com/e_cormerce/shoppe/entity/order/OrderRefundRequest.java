//package com.e_cormerce.shoppe.entity.order;
//
//import com.e_cormerce.shoppe.entity.user.User;
//import com.e_cormerce.shoppe.enums.order.OrderRefundStatus;
//import com.e_cormerce.shoppe.enums.order.OrderRefundType;
//import jakarta.persistence.*;
//import java.math.BigDecimal;
//import java.time.LocalDateTime;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import org.hibernate.annotations.CreationTimestamp;
//import org.hibernate.annotations.SQLDelete;
//import org.hibernate.annotations.UpdateTimestamp;
//import org.hibernate.annotations.Where;
//
//@Entity
//@Table(
//    name = "order_refund_requests",
//    indexes = {@Index(name = "idx_order_refund_requests_seller", columnList = "seller_id")})
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@SQLDelete(sql = "UPDATE order_refund_requests SET deleted=true where id=?")
//@Where(clause = "deleted = false")
//public class OrderRefundRequest {
//  @Id
//  @GeneratedValue(strategy = GenerationType.UUID)
//  String id;
//
//  @Column(columnDefinition = "boolean default false")
//  boolean deleted;
//
//  @Column(nullable = false, columnDefinition = "TEXT")
//  String reason;
//
//  @Column(name = "refund_amount", nullable = false)
//  BigDecimal refundAmount;
//
//  @Column(
//      name = "created_at",
//      nullable = false,
//      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//  @CreationTimestamp
//  LocalDateTime createdAt;
//
//  @UpdateTimestamp
//  @Column(name = "updated_at")
//  LocalDateTime updatedAt;
//
//  @Column(name = "resolved_at")
//  LocalDateTime resolvedAt;
//
//  @Enumerated(EnumType.STRING)
//  @Column(
//      name = "refund_type",
//      columnDefinition = "ENUM('CLIENT_REFUND', 'SHIPPER_REFUND')",
//      nullable = false)
//  OrderRefundType refundType;
//
//  @Enumerated(EnumType.STRING)
//  @Column(columnDefinition = "ENUM('PENDING', 'REJECTED', 'APPROVED')", nullable = false)
//  OrderRefundStatus status;
//
//  @ManyToOne
//  @JoinColumn(name = "requester_id")
//  User requester;
//
//  @ManyToOne
//  @JoinColumn(name = "seller_id")
//  User seller;
//
//  @ManyToOne
//  @JoinColumn(name = "order_id")
//  Order order;
//}
