package com.e_cormerce.shoppe.repository.user;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.dto.response.account.UserProfileResponse;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.projection.UserDetailManageInfoProjection;
import com.e_cormerce.shoppe.projection.UserManageInfoProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
  boolean existsByUsername(String username);

  Optional<User> findById(String id);

  Optional<User> findByUsername(String username);

  @Query(
      value =
          "SELECT  "
              + "u.id, "
              + "ac.email, "
              + "u.username, "
              + "u.avatar, "
              + "r.val AS role, "
              + "u.phone_number, "
              + "u.dob AS birthDate, "
              + "u.created_at, "
              + "u.province, "
              + "u.district, "
              + "u.ward "
              + "FROM users u "
              + "JOIN accounts ac ON u.id = ac.id "
              + "JOIN role r ON ac.role_id = r.id "
              + "WHERE u.id = :id;",
      nativeQuery = true)
  Optional<UserProfileResponse> getUserProfileById(String id);

  @Query(
      value =
          "SELECT  "
              + "u.id, "
              + "u.username, "
              + "u.avatar, "
              + "r.val AS role "
              + "FROM users u "
              + "JOIN accounts ac ON u.id = ac.id "
              + "JOIN role r ON ac.role_id = r.id "
              + "WHERE u.id = :id;",
      nativeQuery = true)
  Optional<UserDto> getUserDto(String id);

  @Query(value = "CALL sp_get_seller_detail_info(:sellerId)", nativeQuery = true)
  UserDetailManageInfoProjection getRegisteredSellerDetailInfo(@Param("sellerId") String sellerId);

  @Query(value = "CALL sp_get_client_detail_info(:clientId)", nativeQuery = true)
  UserDetailManageInfoProjection getClientDetailInfo(@Param("clientId") String clientId);

  @Query(value = "CALL sp_get_seller_info(:limit, :offset)", nativeQuery = true)
  List<UserManageInfoProjection> getSellerInfo(
      @Param("limit") int limit, @Param("offset") int offset);

  @Query(value = "CALL sp_get_client_info(:limit, :offset)", nativeQuery = true)
  List<UserManageInfoProjection> getClientInfo(
      @Param("limit") int limit, @Param("offset") int offset);
}
