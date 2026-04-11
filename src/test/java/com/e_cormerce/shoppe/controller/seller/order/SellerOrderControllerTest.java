package com.e_cormerce.shoppe.controller.seller.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.e_cormerce.shoppe.controller.seller.SellerController;
import com.e_cormerce.shoppe.dto.common.order.OrderTrackingLocationDTO;
import com.e_cormerce.shoppe.dto.response.order.UpdateOrderTrackingLocationResponse;
import com.e_cormerce.shoppe.filter.AuthFilter;
import com.e_cormerce.shoppe.service.order.OrderService;
import com.e_cormerce.shoppe.service.order.OrderTrackingService;
import com.e_cormerce.shoppe.service.seller.SellerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = {SellerController.class},
    excludeAutoConfiguration = {
      SecurityAutoConfiguration.class,
      SecurityFilterAutoConfiguration.class
    },
    excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class SellerOrderControllerTest {
  @Autowired MockMvc mockMvc;
  @Autowired ObjectMapper objectMapper;

  @MockitoBean SellerService sellerService;
  @MockitoBean OrderService orderService;
  @MockitoBean OrderTrackingService orderTrackingService;

  // Tests for updateTracking endpoint (/{orderId}/orders/trackings)
  @Test
  @WithMockUser(authorities = "PERMISSION_UPDATE_ORDER_TRACKING_LOCATION")
  public void testUpdateTrackingSuccess() throws Exception {
    String orderId = "order-123";
    var request = DataTestUpdateOrderTrackingHelper.validUpdateRequest();
    var locationDTO =
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Hoàn Kiếm, Tràng Tiền")
            .arrivedAt(LocalDateTime.of(2024, 4, 10, 10, 30, 0))
            .remainingDistance(500L)
            .build();
    var response = UpdateOrderTrackingLocationResponse.builder().locationDTO(locationDTO).build();

    Mockito.when(orderTrackingService.updateTracking(orderId, request)).thenReturn(response);

    mockMvc
        .perform(
            post("/seller/{orderId}/orders/trackings", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Tracking updated successfully"))
        .andExpect(jsonPath("$.data.locationDTO").exists())
        .andExpect(jsonPath("$.data.locationDTO.remainingDistance").value(500));

    Mockito.verify(orderTrackingService).updateTracking(orderId, request);
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_UPDATE_ORDER_TRACKING_LOCATION")
  public void testUpdateTrackingWithProcessingStatus() throws Exception {
    String orderId = "order-456";
    var request = DataTestUpdateOrderTrackingHelper.processingStatusRequest();
    var locationDTO =
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Ba Đình, Phúc Tân")
            .arrivedAt(LocalDateTime.of(2024, 4, 10, 14, 45, 0))
            .remainingDistance(300L)
            .build();
    var response = UpdateOrderTrackingLocationResponse.builder().locationDTO(locationDTO).build();

    Mockito.when(orderTrackingService.updateTracking(orderId, request)).thenReturn(response);

    mockMvc
        .perform(
            post("/seller/{orderId}/orders/trackings", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Tracking updated successfully"))
        .andExpect(jsonPath("$.data.locationDTO.remainingDistance").value(300));

    Mockito.verify(orderTrackingService).updateTracking(orderId, request);
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_UPDATE_ORDER_TRACKING_LOCATION")
  public void testUpdateTrackingWithOutForDeliveryStatus() throws Exception {
    String orderId = "order-789";
    var request = DataTestUpdateOrderTrackingHelper.outForDeliveryRequest();
    var locationDTO =
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Cầu Giấy, Yên Hòa")
            .arrivedAt(LocalDateTime.of(2024, 4, 10, 18, 0, 0))
            .remainingDistance(100L)
            .build();
    var response = UpdateOrderTrackingLocationResponse.builder().locationDTO(locationDTO).build();

    Mockito.when(orderTrackingService.updateTracking(orderId, request)).thenReturn(response);

    mockMvc
        .perform(
            post("/seller/{orderId}/orders/trackings", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.data.locationDTO.remainingDistance").value(100));

    Mockito.verify(orderTrackingService).updateTracking(orderId, request);
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_UPDATE_ORDER_TRACKING_LOCATION")
  public void testUpdateTrackingWithDeliveredStatus() throws Exception {
    String orderId = "order-999";
    var request = DataTestUpdateOrderTrackingHelper.deliveredRequest();
    var locationDTO =
        OrderTrackingLocationDTO.builder()
            .address("Hà Nội, Đống Đa, Phương Liên")
            .arrivedAt(LocalDateTime.of(2024, 4, 11, 14, 30, 0))
            .remainingDistance(0L)
            .build();
    var response = UpdateOrderTrackingLocationResponse.builder().locationDTO(locationDTO).build();

    Mockito.when(orderTrackingService.updateTracking(orderId, request)).thenReturn(response);

    mockMvc
        .perform(
            post("/seller/{orderId}/orders/trackings", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("Tracking updated successfully"))
        .andExpect(jsonPath("$.data.locationDTO.remainingDistance").value(0));

    Mockito.verify(orderTrackingService).updateTracking(orderId, request);
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_UPDATE_ORDER_TRACKING_LOCATION")
  public void testUpdateTrackingWithMissingAddress() throws Exception {
    String orderId = "order-invalid-1";
    var request = DataTestUpdateOrderTrackingHelper.missingAddressRequest();

    mockMvc
        .perform(
            post("/seller/{orderId}/orders/trackings", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_UPDATE_ORDER_TRACKING_LOCATION")
  public void testUpdateTrackingWithMissingStatus() throws Exception {
    String orderId = "order-invalid-2";
    var request = DataTestUpdateOrderTrackingHelper.missingStatusRequest();

    mockMvc
        .perform(
            post("/seller/{orderId}/orders/trackings", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_UPDATE_ORDER_TRACKING_LOCATION")
  public void testUpdateTrackingWithMissingDistance() throws Exception {
    String orderId = "order-invalid-3";
    var request = DataTestUpdateOrderTrackingHelper.missingDistanceRequest();

    mockMvc
        .perform(
            post("/seller/{orderId}/orders/trackings", orderId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isBadRequest());
  }

  @Test
  public void testUpdateTrackingWithoutAuthentication() throws Exception {
    var request = DataTestUpdateOrderTrackingHelper.validUpdateRequest();

    mockMvc
        .perform(
            post("/seller/order-123/orders/trackings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isUnauthorized());
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_VIEW_ORDERS")
  public void testUpdateTrackingWithWrongPermission() throws Exception {
    var request = DataTestUpdateOrderTrackingHelper.validUpdateRequest();

    mockMvc
        .perform(
            post("/seller/order-123/orders/trackings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
        .andExpect(status().isForbidden());
  }
}
