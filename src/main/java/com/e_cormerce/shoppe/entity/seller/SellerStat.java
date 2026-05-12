// package com.e_cormerce.shoppe.entity.seller;
//
// import com.e_cormerce.shoppe.entity.user.User;
// import jakarta.persistence.*;
// import jakarta.validation.constraints.DecimalMax;
// import jakarta.validation.constraints.DecimalMin;
// import lombok.*;
// import lombok.experimental.FieldDefaults;
// import org.hibernate.annotations.CreationTimestamp;
// import org.hibernate.annotations.SQLDelete;
// import org.hibernate.annotations.SQLRestriction;
// import org.hibernate.annotations.UpdateTimestamp;
//
// import java.math.BigDecimal;
// import java.time.LocalDateTime;
// import java.util.Date;
//
// @Entity
// @Table(
//        name = "seller_stats",
//        indexes = {@Index(name = "idx_seller_stat_seller", columnList = "id")})
// @Getter
// @Setter
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// @FieldDefaults(level = AccessLevel.PRIVATE)
// @SQLDelete(sql = "UPDATE seller_info SET deleted=true where id=?")
// @SQLRestriction("deleted = false")
// public class SellerStat {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    String id;
//
//
//    @Column(name = "rating", nullable = true)
//    @DecimalMin("0.0")
//    @DecimalMax("5.0")
//    Float rating; // null khi chua co luot rating nao.
//
//    @Column(nullable = false, name = "total_products", columnDefinition = "int default 0")
//    int totalProducts;
//
//    @Column(nullable = false, name = "active_products", columnDefinition = "int default 0")
//    int activeProducts;
//
//    @Column(nullable = false, name = "pending_products", columnDefinition = "int default 0")
//    int pendingProducts;
//
//    @Column(nullable = false, name = "banned_products", columnDefinition = "int default 0")
//    int bannedProducts;
//
//    @Column(nullable = false, name = "total_orders", columnDefinition = "int default 0")
//    int totalOrders;
//
//    @Column(nullable = false, name = "pending_orders", columnDefinition = "int default 0")
//    int pendingOrders;
//
//    @Column(nullable = false, name = "approved_orders", columnDefinition = "int default 0")
//    int approvedOrders;
//
//    @Column(nullable = false, name = "shipping_orders", columnDefinition = "int default 0")
//    int shippingOrders;
//
//    @Column(nullable = false, name = "arrived_orders", columnDefinition = "int default 0")
//    int arrivedOrders;
//
//    @Column(nullable = false, name = "cancelled_orders_by_client", columnDefinition = "int default
// 0")
//    int cancelledOrdersByClient;
//
//    @Column(nullable = false, name = "cancelled_orders_by_seller", columnDefinition = "int default
// 0")
//    int cancelledOrdersBySeller;
//
//    @Column(nullable = false, name = "completed_orders", columnDefinition = "int default 0")
//    int completedOrders;
//
//    @Column(nullable = false, name = "refund_requested_orders", columnDefinition = "int default
// 0")
//    int refundRequestedOrders;
//
//    @Column(nullable = false, name = "refunded_orders", columnDefinition = "int default 0")
//    int refundedOrders;
//
//    @Column(nullable = false, name = "failed_delivery_orders", columnDefinition = "int default 0")
//    int failedDeliveryOrders;
//
//    @Column(name = "total_revenue", precision = 15, scale = 2, columnDefinition = "decimal default
// 0")
//    BigDecimal totalRevenue;
//
//    @CreationTimestamp
//    @Column(
//            name = "created_at",
//            updatable = false,
//            columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//    LocalDateTime createdAt;
//
//    @UpdateTimestamp
//    @Column(name = "updated_at")
//    Date updatedAt;
//
//    @Column(columnDefinition = "boolean default false")
//    boolean deleted;
//
//    // owner side
//    @OneToOne(fetch = FetchType.LAZY)
//    @MapsId
//    @JoinColumn(name = "id")
//    User seller;
//
//    @Column(nullable = false)
//    String shopId; // cho api ben thu 3 biet.
// }
