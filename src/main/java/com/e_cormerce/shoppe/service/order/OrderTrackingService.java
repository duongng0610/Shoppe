//package com.e_cormerce.shoppe.service.order;
//
//import com.e_cormerce.shoppe.dto.common.order.OrderTrackingLocationDTO;
//import com.e_cormerce.shoppe.dto.response.order.GetCurrentTrackingResponse;
//import com.e_cormerce.shoppe.entity.order.Order;
//import com.e_cormerce.shoppe.entity.order.OrderTrackingLocation;
//import com.e_cormerce.shoppe.enums.ErrorCode;
//import com.e_cormerce.shoppe.exception.AppException;
//import com.e_cormerce.shoppe.mapper.order.OrderTrackingLocationMapper;
//import com.e_cormerce.shoppe.repository.order.OrderRepository;
//import com.e_cormerce.shoppe.repository.order.OrderTrackingLocationRepository;
//import com.e_cormerce.shoppe.service.auth.AuthService;
//import com.e_cormerce.shoppe.service.order.helper.OrderTrackingHelper;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.context.ApplicationEventPublisher;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Transactional
//@Slf4j
//public class OrderTrackingService {
//    OrderTrackingHelper orderTrackingHelper;
//    OrderRepository orderRepository;
//    ApplicationEventPublisher eventPublisher;
//    OrderTrackingLocationMapper orderTrackingLocationMapper;
//    OrderTrackingLocationRepository orderTrackingLocationRepository;
//    AuthService authService;
//
//
//    public GetCurrentTrackingResponse getOrderShippingState(String orderId) {
//        Order o = orderRepository.findById(orderId).orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));
//        if (!authService.getUserId().equals(o.getClient().getId()) && !authService.getUserId().equals(o.getSeller().getId())) {
//            throw new AppException(ErrorCode.UNAUTHORIZED);
//        }
//        List<OrderTrackingLocation> locations =
//                orderTrackingLocationRepository.findAllByOrderId(orderId);
//        List<OrderTrackingLocationDTO> locationDTOS =
//                locations.stream().map(orderTrackingLocationMapper::toDTO).toList();
//
//        return GetCurrentTrackingResponse.builder().locationDTOS(locationDTOS).build();
//    }
//}
