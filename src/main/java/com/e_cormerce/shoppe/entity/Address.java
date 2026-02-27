package com.e_cormerce.shoppe.entity;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(indexes = {@Index(name = "idx_province", columnList = "province"),
        @Index(name = "idx_province_district", columnList = "province, district"),
        @Index(name = "idx_province_district_ward", columnList = "province, district, ward")
})
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;
    String province;
    String district;
    String ward;
    boolean is_default;

    @Column(columnDefinition = "boolean default false")
    boolean deleted;

    // inverse side
    @ManyToMany(mappedBy = "addresses")
    Set<User> users;
}
