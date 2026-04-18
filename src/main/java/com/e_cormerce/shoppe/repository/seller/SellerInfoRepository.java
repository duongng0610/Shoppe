package com.e_cormerce.shoppe.repository.seller;

import com.e_cormerce.shoppe.dto.response.seller.SellerInfoResponse;
import com.e_cormerce.shoppe.entity.seller.SellerInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

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

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.totalProducts = s.totalProducts + 1,
                    s.pendingProducts = s.pendingProducts + 1
                WHERE s.seller.id = :sellerId
            """)
    void increasePendingProduct(Long sellerId);

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.pendingProducts = s.pendingProducts - 1,
                    s.activeProducts = s.activeProducts + 1
                WHERE s.seller.id = :sellerId
            """)
    void approveProduct(Long sellerId);

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.activeProducts = s.activeProducts - 1,
                    s.bannedProducts = s.bannedProducts + 1
                WHERE s.seller.id = :sellerId
            """)
    void banProduct(Long sellerId);

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.pendingOrders = s.pendingOrders + 1
                WHERE s.seller.id = :sellerId
            """)
    void increasePendingOrder(Long sellerId);

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.pendingOrders = s.pendingOrders - 1,
                    s.approvedOrders = s.approvedOrders + 1
                WHERE s.seller.id = :sellerId
            """)
    void approveOrder(Long sellerId);

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.approvedOrders = s.approvedOrders - 1,
                    s.shippingOrders = s.shippingOrders + 1
                WHERE s.seller.id = :sellerId
            """)
    void shippingOrder(Long sellerId);

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.shippingOrders = s.shippingOrders - 1,
                    s.completedOrders = s.completedOrders + 1
                WHERE s.seller.id = :sellerId
            """)
    void completeOrder(Long sellerId);

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.pendingOrders = s.pendingOrders - 1,
                    s.cancelledOrders = s.cancelledOrders + 1
                WHERE s.seller.id = :sellerId
            """)
    void cancelOrder(Long sellerId);

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.completedOrders = s.completedOrders - 1,
                    s.refundRequestedOrders = s.refundRequestedOrders + 1
                WHERE s.seller.id = :sellerId
            """)
    void requestRefund(Long sellerId);

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.refundRequestedOrders = s.refundRequestedOrders - 1,
                    s.refundedOrders = s.refundedOrders + 1
                WHERE s.seller.id = :sellerId
            """)
    void refundOrder(Long sellerId);

    @Modifying
    @Query("""
                UPDATE SellerInfo s
                SET s.shippingOrders = s.shippingOrders - 1,
                    s.failedDeliveryOrders = s.failedDeliveryOrders + 1
                WHERE s.seller.id = :sellerId
            """)
    void failDelivery(Long sellerId);
}
