package com.e_cormerce.shoppe.repository.user;

import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.projection.overview.OverviewOrderProductProjection;
import com.e_cormerce.shoppe.projection.seller.SellerInformationProjection;
import com.e_cormerce.shoppe.projection.user.UserDetailManageInfoProjection;
import com.e_cormerce.shoppe.projection.user.UserManageInfoProjection;
import com.e_cormerce.shoppe.projection.user.UserWithDetailInfoProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByUsername(String username);

    Optional<User> findById(String id);

    Optional<User> findByUsername(String username);

    @Query(
            value =
                    "SELECT * FROM ecommerce.user_with_detail_info where id =:id",
            nativeQuery = true)
    Optional<UserWithDetailInfoProjection> getUserProfileById(String id);

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


    @Query(
            value = """
                    CALL get_seller_information(:sellerId)
                    """,
            nativeQuery = true
    )
    SellerInformationProjection getSellerInformation(
            @Param("sellerId") String sellerId
    );

    @Query(
            value = """
                    CALL get_overview_order_product(
                        :sellerId,
                        :days
                    )
                    """,
            nativeQuery = true
    )
    OverviewOrderProductProjection
    getOverviewOrderProduct(
            @Param("sellerId") String sellerId,
            @Param("days") Integer days
    );

    // admin
    @Query(
            value = """
                    CALL get_overview_order_product(
                        NULL,
                        :days
                    )
                    """,
            nativeQuery = true
    )
    OverviewOrderProductProjection
    getOverviewOrderProductForAdmin(
            @Param("days") Integer days
    );


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
