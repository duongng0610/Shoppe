package com.e_cormerce.shoppe.repository.user;

import com.e_cormerce.shoppe.entity.user.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, String> {
  @Query(
      value =
          "Select * from addresses where province =:province and district =:district and ward =:ward",
      nativeQuery = true)
  Address findByEntireAddress(
      @Param("province") String province,
      @Param("district") String district,
      @Param("ward") String ward);
}
