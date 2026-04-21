package com.e_cormerce.shoppe.entity.order;

import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "orders",
        indexes = {@Index(name = "idx_orders_status", columnList = "status")})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@SQLDelete(sql = "UPDATE orders SET deleted=true where id=?")
@Where(clause = "deleted = false")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    OrderStatus status;

    @Column(name = "total_price", precision = 15, scale = 2, nullable = false)
    BigDecimal totalPrice;


    @Column(nullable = false)
    int quantity;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreationTimestamp
    LocalDateTime createdAt;

    
    @UpdateTimestamp
    @Column(name = "updated_at")
    LocalDateTime updatedAt;


    @ManyToOne
    @JoinColumn(name = "variant_id", nullable = false)
    Variant variant;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    User client;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    User seller;

    @Column(name = "shipping_province")
    String province;

    @Column(name = "shipping_district")
    String district;

    @Column(name = "shipping_ward")
    String ward;

    @Column(name = "shipping_phone_number", nullable = false)
    String shippingPhoneNumber;

    @Column(name = "product_name", nullable = false)
    String productName;

    @Column(name = "variant_thumbnail", nullable = false)
    String variantThumbnail;

    @Column(name = "variant_attributes", columnDefinition = "json", nullable = true)
    String variantAttributes;


}
