package com.e_cormerce.shoppe.repository.order;

import com.e_cormerce.shoppe.entity.order.OrderReservation;
import com.e_cormerce.shoppe.enums.order.ReservationStatus;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderReservationRepository extends JpaRepository<OrderReservation, String> {

  // lấy reservation hết hạn
  List<OrderReservation> findByStatusAndExpireAtBefore(
      ReservationStatus status, LocalDateTime time);

  // lấy theo order
  OrderReservation findByOrderId(String orderId);

  @Query(
      """
                SELECT r FROM OrderReservation r
                JOIN fetch r.variant
                Join fetch r.order
                WHERE r.status = :status
                  AND r.expireAt < :time
            """)
  List<OrderReservation> findExpired(
      @Param("status") ReservationStatus status, @Param("time") LocalDateTime time);
}
