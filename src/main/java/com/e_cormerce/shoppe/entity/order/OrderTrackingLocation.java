//package com.e_cormerce.shoppe.entity.order;
//
//import jakarta.persistence.*;
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
//    name = "order_tracking_locations",
//    indexes = {@Index(name = "idx_order_id", columnList = "order_id")})
//@Getter
//@Setter
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE)
//@SQLDelete(sql = "UPDATE order_tracking_locations SET deleted=true where id=?")
//@Where(clause = "deleted = false")
//public class OrderTrackingLocation {
//  @Id
//  @GeneratedValue(strategy = GenerationType.UUID)
//  String id;
//
//  @Column(columnDefinition = "boolean default false")
//  boolean deleted;
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
//  @Column(name = "address", nullable = false)
//  String address;
//
//  @Column(name = "arrived_at", nullable = false)
//  LocalDateTime arrivedAt;
//
//  @Column(name = "remaining_distance", nullable = false)
//  float remainingDistance;
//
//  @ManyToOne(fetch = FetchType.LAZY)
//  @JoinColumn(name = "order_id", nullable = false)
//  Order order;
//}
