package com.e_cormerce.shoppe.repository.seller;

import com.e_cormerce.shoppe.dto.response.seller.SellerInfoResponse;
import com.e_cormerce.shoppe.entity.seller.SellerStat;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface SellerStatRepository extends JpaRepository<SellerStat, String> {

  Optional<SellerStat> findById(String sellerId);

  @Query(
      value =
          "SELECT  "
              + "u.id, "
              + "u.avatar, "
              + "u.username, "
              + "u.phone_number, "
              + "s.rating, "
              + "s.active_products, "
              + "s.created_at, "
              + "a.province_name, "
              + "a.district_name, "
              + "a.ward_name "
              + "FROM seller_stats s "
              + "JOIN users u ON s.id = u.id "
              + "Join address a on a.id = u.address_id "
              + "WHERE s.id = :sellerId;",
      nativeQuery = true)
  Optional<SellerInfoResponse> findSellerInfoBySellerId(String sellerId);

  @Query(value = "SELECT shop_id from seller_stats s  WHERE s.id = :sellerId", nativeQuery = true)
  Optional<String> findShopIdBySellerId(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.totalProducts = s.totalProducts + 1 ,
                    s.pendingProducts = s.pendingProducts + 1
                WHERE s.seller.id = :sellerId
            """)
  void createProduct(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                 UPDATE SellerStat s
                  SET s.totalProducts = s.totalProducts - 1,
            s.activeProducts = s.activeProducts - 1
                 WHERE s.seller.id = :sellerId
            """)
  void deleteProduct(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.pendingProducts = s.pendingProducts - 1,
                    s.activeProducts = s.activeProducts + 1
                WHERE s.seller.id = :sellerId
            """)
  void approveProduct(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.pendingProducts = s.pendingProducts - 1
                WHERE s.seller.id = :sellerId
            """)
  void rejectProduct(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.activeProducts = s.activeProducts - 1,
                    s.bannedProducts = s.bannedProducts + 1
                WHERE s.seller.id = :sellerId
            """)
  void banProduct(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.activeProducts = s.activeProducts + 1,
                    s.bannedProducts = s.bannedProducts - 1
                WHERE s.seller.id = :sellerId
            """)
  void unlockProduct(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.activeProducts = s.activeProducts - 1
                WHERE s.seller.id = :sellerId
            """)
  void hiddenProduct(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.activeProducts = s.activeProducts + 1
                WHERE s.seller.id = :sellerId
            """)
  void unhiddenProduct(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.pendingOrders = s.pendingOrders + 1
                , s.totalOrders = s.totalOrders+1
                WHERE s.seller.id = :sellerId
            """)
  void createOrder(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.pendingOrders = s.pendingOrders - 1,
                    s.approvedOrders = s.approvedOrders + 1
                WHERE s.seller.id = :sellerId
            """)
  void approveOrder(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.approvedOrders = s.approvedOrders - 1,
                    s.shippingOrders = s.shippingOrders + 1
                WHERE s.seller.id = :sellerId
            """)
  void shippingOrder(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.shippingOrders = s.shippingOrders - 1,
                    s.completedOrders = s.completedOrders + 1,
                    s.totalRevenue = s.totalRevenue + :amount
                WHERE s.seller.id = :sellerId
            """)
  void completeOrder(String sellerId, BigDecimal amount);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.pendingOrders = s.pendingOrders - 1,
                    s.cancelledOrdersByClient = s.cancelledOrdersByClient + 1
                WHERE s.seller.id = :sellerId
            """)
  void cancelOrderByClient(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.pendingOrders = s.pendingOrders - 1,
                    s.cancelledOrdersBySeller = s.cancelledOrdersBySeller + 1
                WHERE s.seller.id = :sellerId
            """)
  void cancelOrderBySeller(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.completedOrders = s.completedOrders - 1,
                    s.refundRequestedOrders = s.refundRequestedOrders + 1
                WHERE s.seller.id = :sellerId
            """)
  void requestRefund(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.refundRequestedOrders = s.refundRequestedOrders - 1,
                    s.refundedOrders = s.refundedOrders + 1
                WHERE s.seller.id = :sellerId
            """)
  void refundOrder(String sellerId);

  @Modifying
  @Transactional
  @Query(
      """
                UPDATE SellerStat s
                SET s.shippingOrders = s.shippingOrders - 1,
                    s.failedDeliveryOrders = s.failedDeliveryOrders + 1
                WHERE s.seller.id = :sellerId
            """)
  void failDelivery(String sellerId);
}
