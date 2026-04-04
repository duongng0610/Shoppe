package com.e_cormerce.shoppe.entity.seller;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(
    name = "seller_info",
    indexes = {@Index(name = "idx_seller_info_seller", columnList = "id")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE shop_info SET deleted=true where id=?")
@SQLRestriction("deleted = false")
public class SellerInfo {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(columnDefinition = "int default 0")
  int follower;

  @Column(columnDefinition = "float default 0")
  float rating;

  @Column(name = "product_count", columnDefinition = "int default 0")
  int productCount;

  @CreationTimestamp
  @Column(name = "created_at", updatable = false)
  LocalDateTime createdAt;

  @Column(columnDefinition = "boolean default false")
  boolean deleted;

  // owner side
  @OneToOne(fetch = FetchType.LAZY)
  @MapsId
  @JoinColumn(name = "id")
  User seller;
}
