//package com.e_cormerce.shoppe.repository.order;
//
//import com.e_cormerce.shoppe.entity.order.OrderTrackingLocation;
//import java.util.List;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
//
//@Repository
//public interface OrderTrackingLocationRepository
//    extends JpaRepository<OrderTrackingLocation, String> {
//
//  @Query(
//      value =
//          "SELECT * FROM order_tracking_locations "
//              + "WHERE ot.order_id = :order_id "
//              + "ORDER BY ot.created_at DESC",
//      nativeQuery = true)
//  List<OrderTrackingLocation> findAllByOrderId(@Param("order_id") String orderId);
//}
