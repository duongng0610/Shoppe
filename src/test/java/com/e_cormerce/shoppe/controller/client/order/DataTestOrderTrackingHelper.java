package com.e_cormerce.shoppe.controller.client.order;

import com.e_cormerce.shoppe.dto.common.order.OrderTrackingLocationDTO;
import com.e_cormerce.shoppe.dto.response.order.GetCurrentTrackingResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataTestOrderTrackingHelper {

  /** Creates a valid list of OrderTrackingLocationDTO with sample tracking locations */
  public static GetCurrentTrackingResponse validTrackingLocationsList() {
    List<OrderTrackingLocationDTO> locations = new ArrayList<>();
    locations.add(
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Hoàn Kiếm, Tràng Tiền")
            .arrivedAt(LocalDateTime.of(2024, 4, 10, 10, 30, 0))
            .remainingDistance(500L)
            .build());
    locations.add(
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Ba Đình, Phúc Tân")
            .arrivedAt(LocalDateTime.of(2024, 4, 10, 14, 45, 0))
            .remainingDistance(300L)
            .build());
    locations.add(
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Cầu Giấy, Yên Hòa")
            .arrivedAt(LocalDateTime.of(2024, 4, 10, 18, 0, 0))
            .remainingDistance(100L)
            .build());

    return GetCurrentTrackingResponse.builder().locationDTOS(locations).build();
  }

  /** Creates an empty list of tracking locations */
  public static GetCurrentTrackingResponse emptyTrackingLocationsList() {
    return GetCurrentTrackingResponse.builder().locationDTOS(new ArrayList<>()).build();
  }

  /** Creates a list with single tracking location */
  public static GetCurrentTrackingResponse singleLocationList() {
    List<OrderTrackingLocationDTO> locations = new ArrayList<>();
    locations.add(
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Hoàn Kiếm, Tràng Tiền")
            .arrivedAt(LocalDateTime.of(2024, 4, 10, 8, 0, 0))
            .remainingDistance(1000L)
            .build());

    return GetCurrentTrackingResponse.builder().locationDTOS(locations).build();
  }

  /** Creates a list with delivered status (zero remaining distance) */
  public static GetCurrentTrackingResponse deliveredTrackingLocationsList() {
    List<OrderTrackingLocationDTO> locations = new ArrayList<>();
    locations.add(
        OrderTrackingLocationDTO.builder()
            .address("Hồ Chí Minh, Quận 1, Bến Nghé")
            .arrivedAt(LocalDateTime.of(2024, 4, 11, 10, 30, 0))
            .remainingDistance(0L)
            .build());

    return GetCurrentTrackingResponse.builder().locationDTOS(locations).build();
  }

  /** Creates a list with multiple delivery stages */
  public static GetCurrentTrackingResponse multipleStagesTrackingList() {
    List<OrderTrackingLocationDTO> locations = new ArrayList<>();
    locations.add(
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Hoàn Kiếm, Tràng Tiền")
            .arrivedAt(LocalDateTime.of(2024, 4, 10, 8, 0, 0))
            .remainingDistance(2000L)
            .build());
    locations.add(
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Ba Đình, Phúc Tân")
            .arrivedAt(LocalDateTime.of(2024, 4, 10, 12, 0, 0))
            .remainingDistance(1000L)
            .build());
    locations.add(
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Cầu Giấy, Yên Hòa")
            .arrivedAt(LocalDateTime.of(2024, 4, 11, 8, 0, 0))
            .remainingDistance(200L)
            .build());
    locations.add(
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Đống Đa, Phương Liên")
            .arrivedAt(LocalDateTime.of(2024, 4, 11, 14, 30, 0))
            .remainingDistance(0L)
            .build());

    return GetCurrentTrackingResponse.builder().locationDTOS(locations).build();
  }
}
