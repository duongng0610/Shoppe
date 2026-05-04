//package com.e_cormerce.shoppe.entity.analytic.product.all;
//
//import jakarta.persistence.*;
//import java.math.BigDecimal;
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import lombok.*;
//import lombok.experimental.FieldDefaults;
//import org.hibernate.annotations.UpdateTimestamp;
//
//@Entity
//@Table(
//    name = "product_daily",
//    indexes = {
//      @Index(name = "idx_product", columnList = "product_id,date"),
//      @Index(name = "idx_date", columnList = "date")
//    })
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@IdClass(ProductDailyId.class)
//public class ProductDaily {
//
//  @Id
//  @Column(name = "date", nullable = false)
//  LocalDate date;
//
//  @Column(name = "total_views", nullable = false, columnDefinition = "int default 0")
//  int totalViews;
//
//  @Column(name = "total_orders", nullable = false, columnDefinition = "int default 0")
//  int totalOrders;
//
//  @Column(name = "total_units_orders", nullable = false, columnDefinition = "int default 0")
//  int totalUnitsOrder;
//
//  @Column(name = "total_revenue", precision = 15, scale = 2, columnDefinition = "decimal default 0")
//  BigDecimal totalRevenue;
//
//  @Column(
//      name = "created_at",
//      nullable = false,
//      updatable = false,
//      insertable = false,
//      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
//  LocalDateTime createdAt;
//
//  @UpdateTimestamp
//  @Column(name = "updated_at")
//  LocalDateTime updatedAt;
//
//  @Id
//  @Column(name = "product_id", nullable = false)
//  String productId;
//}
