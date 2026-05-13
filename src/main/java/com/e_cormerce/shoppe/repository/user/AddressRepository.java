package com.e_cormerce.shoppe.repository.user;

import com.e_cormerce.shoppe.dto.response.address.DistrictDto;
import com.e_cormerce.shoppe.dto.response.address.ProvinceDto;
import com.e_cormerce.shoppe.dto.response.address.WardDto;
import com.e_cormerce.shoppe.entity.user.Address;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface AddressRepository extends JpaRepository<Address, String> {

  // get province
  @Query(
      value =
          """
                                SELECT DISTINCT
                                    a.province_id AS id,
                                    a.province_name AS name
                                FROM address a
                            """,
      nativeQuery = true)
  List<ProvinceDto> getProvinces();

  // tìm theo province
  @Query(
      value =
          """
                                SELECT DISTINCT
                                    a.district_id AS id,
                                    a.district_name AS name

                                FROM address a
                                WHERE a.province_id = :provinceId
                            """,
      nativeQuery = true)
  List<DistrictDto> findByProvinceId(Integer provinceId);

  // tìm theo district
  @Query(
      value =
          """
                                SELECT DISTINCT
                                    a.ward_id AS id,
                                    a.ward_name AS name

                                FROM address a
                                WHERE a.district_id = :districtId
                            """,
      nativeQuery = true)
  List<WardDto> findByDistrictId(Integer districtId);

  // tìm theo full name
  Optional<Address> findByProvinceNameAndDistrictNameAndWardName(
      String provinceName, String districtName, String wardName);
}
