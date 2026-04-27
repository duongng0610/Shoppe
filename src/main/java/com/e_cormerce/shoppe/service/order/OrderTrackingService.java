package com.e_cormerce.shoppe.service.order;

import com.e_cormerce.shoppe.dto.common.order.OrderTrackingLocationDTO;
import com.e_cormerce.shoppe.dto.request.order.UpdateOrderTrackingLocationRequest;
import com.e_cormerce.shoppe.dto.response.order.GetCurrentTrackingResponse;
import com.e_cormerce.shoppe.dto.response.order.UpdateOrderTrackingLocationResponse;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.order.OrderTrackingLocation;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.event.order.OrderLocationUpdated;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.order.OrderTrackingLocationMapper;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.repository.order.OrderTrackingLocationRepository;
import com.e_cormerce.shoppe.service.order.helper.OrderTrackingHelper;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Transactional
@Slf4j
public class OrderTrackingService {
  OrderTrackingHelper orderTrackingHelper;
  OrderRepository orderRepository;
  ApplicationEventPublisher eventPublisher;
  OrderTrackingLocationMapper orderTrackingLocationMapper;
  OrderTrackingLocationRepository orderTrackingLocationRepository;

  public UpdateOrderTrackingLocationResponse updateTracking(
      String orderId, UpdateOrderTrackingLocationRequest request) {

    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));

    User client = orderTrackingHelper.getClient();

    OrderTrackingLocation location =
        orderTrackingHelper.createOrderTrackingLocation(request, order);

    if (order.getStatus() != OrderStatus.SHIPPING) {
      throw new AppException(ErrorCode.UNABLE_UPDATE_TRACKING);
    }

    OrderStatus newStatus = OrderStatus.fromString(request.getOrderStatus());

    //        if (newStatus == OrderStatus.ARRIVED) {
    //            order.setStatus(OrderStatus.ARRIVED);
    //            orderRepository.save(order);
    //        }

    OrderTrackingLocation saved = orderTrackingLocationRepository.save(location);

    eventPublisher.publishEvent(OrderLocationUpdated.builder().address(saved.getAddress()).build());

    return UpdateOrderTrackingLocationResponse.builder()
        .locationDTO(orderTrackingLocationMapper.toDTO(saved))
        .build();
  }

  public GetCurrentTrackingResponse getCurrentTracking(String orderId) {
    List<OrderTrackingLocation> locations =
        orderTrackingLocationRepository.findAllByOrderId(orderId);
    List<OrderTrackingLocationDTO> locationDTOS =
        locations.stream().map(orderTrackingLocationMapper::toDTO).toList();

    return GetCurrentTrackingResponse.builder().locationDTOS(locationDTOS).build();
  }
}
