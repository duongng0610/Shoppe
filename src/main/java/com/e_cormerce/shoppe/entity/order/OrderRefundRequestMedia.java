package com.e_cormerce.shoppe.entity.order;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.SQLDelete;
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
}
