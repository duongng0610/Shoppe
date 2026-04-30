package com.e_cormerce.shoppe.controller.seller.order;

import com.e_cormerce.shoppe.dto.request.order.UpdateOrderTrackingLocationRequest;
import com.e_cormerce.shoppe.enums.order.OrderStatus;

public class DataTestUpdateOrderTrackingHelper {

  /** Creates a valid UpdateOrderTrackingLocationRequest */
  public static UpdateOrderTrackingLocationRequest validUpdateRequest() {
    return UpdateOrderTrackingLocationRequest.builder()
        .orderStatus(OrderStatus.SHIPPING.name())
        .address("Hà Nội, Hoàn Kiếm, Tràng Tiền")
        .remainingDistance(500L)
        .build();
  }

  /** Creates an UpdateOrderTrackingLocationRequest with "Processing" status */
  public static UpdateOrderTrackingLocationRequest processingStatusRequest() {
    return UpdateOrderTrackingLocationRequest.builder()
        .orderStatus(OrderStatus.ACCEPTED.name())
        .address("Hà Nội, Ba Đình, Phúc Tân")
        .remainingDistance(300L)
        .build();
  }

  /** Creates an UpdateOrderTrackingLocationRequest with "Out for Delivery" status */
  public static UpdateOrderTrackingLocationRequest outForDeliveryRequest() {
    return UpdateOrderTrackingLocationRequest.builder()
       // .orderStatus(OrderStatus.ARRIVED.name())
        .address("Hà Nội, Cầu Giấy, Yên Hòa")
        .remainingDistance(100L)
        .build();
  }

  /** Creates an UpdateOrderTrackingLocationRequest with "Delivered" status */
  public static UpdateOrderTrackingLocationRequest deliveredRequest() {
    return UpdateOrderTrackingLocationRequest.builder()
        //.orderStatus(OrderStatus.PAID.name())
        .address("Hà Nội, Đống Đa, Phương Liên")
        .remainingDistance(0L)
        .build();
  }

  /** Creates an UpdateOrderTrackingLocationRequest with missing address (invalid) */
  public static UpdateOrderTrackingLocationRequest missingAddressRequest() {
    return UpdateOrderTrackingLocationRequest.builder()
        .orderStatus(OrderStatus.SHIPPING.name())
        .remainingDistance(500L)
        .build();
  }

  /** Creates an UpdateOrderTrackingLocationRequest with missing status (invalid) */
  public static UpdateOrderTrackingLocationRequest missingStatusRequest() {
    return UpdateOrderTrackingLocationRequest.builder()
        .address("Hà Nội, Hoàn Kiếm, Tràng Tiền")
        .remainingDistance(500L)
        .build();
  }

  /** Creates an UpdateOrderTrackingLocationRequest with missing remainingDistance (invalid) */
  public static UpdateOrderTrackingLocationRequest missingDistanceRequest() {
    return UpdateOrderTrackingLocationRequest.builder()
        .orderStatus(OrderStatus.SHIPPING.name())
        .address("Hà Nội, Hoàn Kiếm, Tràng Tiền")
        .build();
  }
}
