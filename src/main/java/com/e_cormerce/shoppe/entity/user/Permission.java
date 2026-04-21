package com.e_cormerce.shoppe.entity.user;

import com.e_cormerce.shoppe.enums.user.PermissionEnum;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(
        name = "permissions",
        indexes = {@Index(name = "idx_permissions_val", columnList = "val")})
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    PermissionEnum val;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    Date updatedAt;

}
