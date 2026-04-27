package com.e_cormerce.shoppe.entity.order;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

@Entity
@Table(
    name = "order_refund_request_medias",
    indexes = {@Index(name = "idx_order_refund_request", columnList = "order_refund_request_id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE order_refund_request_medias SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class OrderRefundRequestMedia {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  @Column(nullable = false)
  String url;

  @ManyToOne
  @JoinColumn(name = "order_refund_request_id")
  OrderRefundRequest orderRefundRequest;

  @Column(
      name = "created_at",
      nullable = false,
      columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
  @CreationTimestamp
  LocalDateTime createdAt;

  @UpdateTimestamp
  @Column(name = "updated_at")
  LocalDateTime updatedAt;
}
