package com.e_cormerce.shoppe.entity.seller;

import com.e_cormerce.shoppe.entity.user.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.Date;

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

    @Column(name = "followers", columnDefinition = "int default 0")
    Integer follower;

    @Column(name = "rating", nullable = true)
    Float rating;//null khi chua co luot rating nao.

    @Column(name = "total_products", columnDefinition = "int default 0")
    Integer totalProducts;


    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    LocalDateTime createdAt;

    @Column(name = "updated_at")
    Date updatedAt;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    // owner side
    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "id")
    User seller;
}
