//package com.e_cormerce.shoppe.service.order.helper;
//
//import com.e_cormerce.shoppe.dto.request.order.UpdateOrderTrackingLocationRequest;
//import com.e_cormerce.shoppe.entity.order.Order;
//import com.e_cormerce.shoppe.entity.user.User;
//import com.e_cormerce.shoppe.service.auth.AuthService;
//import java.time.LocalDateTime;
//import lombok.AccessLevel;
//import lombok.RequiredArgsConstructor;
//import lombok.experimental.FieldDefaults;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.stereotype.Component;
//import org.springframework.validation.annotation.Validated;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Component
//@RequiredArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
//@Validated
//@Slf4j
//public class OrderTrackingHelper {
//
//  WebClient.Builder webClientBuilder;
//  AuthService authService;
//
//  public OrderTrackingLocation createOrderTrackingLocation(
//      UpdateOrderTrackingLocationRequest request, Order order) {
//
//    OrderTrackingLocation location =
//        OrderTrackingLocation.builder()
//            .order(order)
//            .address(request.getAddress())
//            .arrivedAt(LocalDateTime.now())
//            .remainingDistance(request.getRemainingDistance())
//            .build();
//
//    return location;
//  }
//
//  public User getClient() {
//    return authService.getUserThroughAuthentication();
//  }
//}
