package com.e_cormerce.shoppe.repository.order;

import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.projection.order.OrderRevenueProjection;
import com.e_cormerce.shoppe.projection.user.OrderWithUserInfoProjection;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
  Optional<Order> findById(String id);

  @Query(
      value =
          "SELECT * FROM client_orders_view WHERE client_id = :client_id LIMIT :limit OFFSET :offset",
      nativeQuery = true)
  List<Order> findOrderOfClient(
      @Param("client_id") String clientId, @Param("limit") int limit, @Param("offset") int offset);

  @Query(
      value =
          "SELECT * FROM seller_orders_view WHERE seller_id = :seller_id LIMIT :limit OFFSET :offset",
      nativeQuery = true)
  List<Order> findOrderOfSeller(
      @Param("seller_id") String sellerId, @Param("limit") int limit, @Param("offset") int offset);

  // =====================================
  // SELLER
  // =====================================

  @Query(
      value =
          """
                    SELECT total_revenue_recent(
                        :sellerId,
                        :status,
                        :days
                    ) AS revenue
                    """,
      nativeQuery = true)
  OrderRevenueProjection getSellerRevenue(
      @Param("sellerId") String sellerId,
      @Param("status") String status,
      @Param("days") Integer days);

  // =====================================
  // ADMIN
  // =====================================

  @Query(
      value =
          """
                    SELECT total_revenue_recent(
                        NULL,
                        :status,
                        :days
                    ) AS revenue
                    """,
      nativeQuery = true)
  OrderRevenueProjection getAdminRevenue(
      @Param("status") String status, @Param("days") Integer days);

  @Query(
      value =
          """
            SELECT *
            FROM order_with_user_info
            WHERE clientId = :clientId
            LIMIT :limit OFFSET :offset
            """,
      nativeQuery = true)
  List<OrderWithUserInfoProjection> findOrdersByClientId(
      @Param("clientId") String clientId, @Param("limit") int limit, @Param("offset") int offset);

  @Query(
      value =
          """
            SELECT *
            FROM order_with_user_info
            WHERE sellerId = :sellerId
            LIMIT :limit OFFSET :offset
            """,
      nativeQuery = true)
  List<OrderWithUserInfoProjection> findOrdersBySellerId(
      @Param("sellerId") String sellerId, @Param("limit") int limit, @Param("offset") int offset);
}
