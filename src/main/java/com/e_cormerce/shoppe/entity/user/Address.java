package com.e_cormerce.shoppe.entity.user;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(
    name = "address",
    indexes = {
      @Index(name = "idx_province", columnList = "province_id"),
      @Index(name = "idx_district", columnList = "district_id"),
      @Index(name = "idx_full_name", columnList = "province_name,district_name,ward_name")
    })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private String id;

  @Column(name = "province_name")
  String provinceName;

  @Column(name = "province_id")
  Integer provinceId;

  @Column(name = "district_name")
  String districtName;

  @Column(name = "district_id")
  Integer districtId;

  @Column(name = "ward_name")
  String wardName;

  @Column(name = "ward_id")
  String wardId;
}
