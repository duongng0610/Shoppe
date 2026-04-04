package com.e_cormerce.shoppe.repository.seller;

import com.e_cormerce.shoppe.dto.response.seller.SellerInfoResponse;
import com.e_cormerce.shoppe.entity.seller.SellerInfo;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerInfoRepository extends JpaRepository<SellerInfo, String> {

  Optional<SellerInfo> findById(String sellerId);

  @Query(
      value =
          "SELECT  "
              + "s.id, "
              + "u.username, "
              + "u.avatar, "
              + "u.phone_number, "
              + "s.follower, "
              + "s.rating, "
              + "s.product_count, "
              + "s.created_at, "
              + "a.province, "
              + "a.district, "
              + "a.ward "
              + "FROM seller_info s "
              + "JOIN users u ON s.id = u.id "
              + "Join addresses a on a.id = u.address_id "
              + "WHERE s.id = :sellerId;",
      nativeQuery = true)
  Optional<SellerInfoResponse> findSellerInfoBySellerId(String sellerId);
}
