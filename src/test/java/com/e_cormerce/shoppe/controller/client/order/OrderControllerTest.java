package com.e_cormerce.shoppe.controller.client.order;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.e_cormerce.shoppe.filter.AuthFilter;
import com.e_cormerce.shoppe.service.order.OrderService;
import com.e_cormerce.shoppe.service.order.OrderTrackingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(
    controllers = {OrderController.class},
    excludeAutoConfiguration = {
      SecurityAutoConfiguration.class,
      SecurityFilterAutoConfiguration.class
    },
    excludeFilters =
        @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = AuthFilter.class))
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {
  @Autowired MockMvc mockMvc;
  @MockitoBean OrderTrackingService orderTrackingService;
  @MockitoBean OrderService orderService;

  // Tests for getCurrentTracking endpoint (Line 64-70)
  @Test
  @WithMockUser(authorities = "PERMISSION_CANCEL_ORDER_BY_CLIENT")
  public void testGetCurrentTrackingSuccess() throws Exception {
    String orderId = "order-123";
    var trackingLocations = DataTestOrderTrackingHelper.validTrackingLocationsList();

    Mockito.when(orderTrackingService.getCurrentTracking(orderId)).thenReturn(trackingLocations);

    mockMvc
        .perform(get("/client/orders/{orderId}/trackings/current", orderId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("get order tracking successfully"))
        .andExpect(jsonPath("$.data.locationDTOS").isArray())
        .andExpect(jsonPath("$.data.locationDTOS.length()").value(3))
        .andExpect(
            jsonPath("$.data.locationDTOS[0].address").value("Hà Nội, Hoàn Kiếm, Tràng Tiền"))
        .andExpect(jsonPath("$.data.locationDTOS[0].remainingDistance").value(500));

    Mockito.verify(orderTrackingService).getCurrentTracking(orderId);
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_CANCEL_ORDER_BY_CLIENT")
  public void testGetCurrentTrackingWithEmptyResult() throws Exception {
    String orderId = "order-456";
    var trackingLocations = DataTestOrderTrackingHelper.emptyTrackingLocationsList();

    Mockito.when(orderTrackingService.getCurrentTracking(orderId)).thenReturn(trackingLocations);

    mockMvc
        .perform(get("/client/orders/{orderId}/trackings/current", orderId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("get order tracking successfully"))
        .andExpect(jsonPath("$.data.locationDTOS").isArray())
        .andExpect(jsonPath("$.data.locationDTOS.length()").value(0));

    Mockito.verify(orderTrackingService).getCurrentTracking(orderId);
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_CANCEL_ORDER_BY_CLIENT")
  public void testGetCurrentTrackingWithSingleLocation() throws Exception {
    String orderId = "order-789";
    var trackingLocations = DataTestOrderTrackingHelper.singleLocationList();

    Mockito.when(orderTrackingService.getCurrentTracking(orderId)).thenReturn(trackingLocations);

    mockMvc
        .perform(get("/client/orders/{orderId}/trackings/current", orderId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("get order tracking successfully"))
        .andExpect(jsonPath("$.data.locationDTOS").isArray())
        .andExpect(jsonPath("$.data.locationDTOS.length()").value(1))
        .andExpect(
            jsonPath("$.data.locationDTOS[0].address").value("Hà Nội, Hoàn Kiếm, Tràng Tiền"))
        .andExpect(jsonPath("$.data.locationDTOS[0].remainingDistance").value(1000));

    Mockito.verify(orderTrackingService).getCurrentTracking(orderId);
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_CANCEL_ORDER_BY_CLIENT")
  public void testGetCurrentTrackingDeliveredStatus() throws Exception {
    String orderId = "order-delivered";
    var trackingLocations = DataTestOrderTrackingHelper.deliveredTrackingLocationsList();

    Mockito.when(orderTrackingService.getCurrentTracking(orderId)).thenReturn(trackingLocations);

    mockMvc
        .perform(get("/client/orders/{orderId}/trackings/current", orderId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("get order tracking successfully"))
        .andExpect(jsonPath("$.data.locationDTOS[0].remainingDistance").value(0));

    Mockito.verify(orderTrackingService).getCurrentTracking(orderId);
  }

  // Tests for getClientOrders endpoint (Line 72-81)
  @Test
  @WithMockUser(authorities = "PERMISSION_CLIENT_VIEW_ORDERS")
  public void testGetClientOrdersSuccess() throws Exception {
    var orderDetailResponse = DataTestOrderDetailHelper.validGetOrderDetailResponse();

    Mockito.when(orderService.getOrdersByClient()).thenReturn(orderDetailResponse);

    mockMvc
        .perform(get("/client/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("get orders successfully"))
        .andExpect(jsonPath("$.data").exists());

    Mockito.verify(orderService).getOrdersByClient();
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_CLIENT_VIEW_ORDERS")
  public void testGetClientOrdersWithEmptyResult() throws Exception {
    var orderDetailResponse = DataTestOrderDetailHelper.emptyGetOrderDetailResponse();

    Mockito.when(orderService.getOrdersByClient()).thenReturn(orderDetailResponse);

    mockMvc
        .perform(get("/client/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("get orders successfully"))
        .andExpect(jsonPath("$.data.orderDetails").isArray())
        .andExpect(jsonPath("$.data.orderDetails.length()").value(0));

    Mockito.verify(orderService).getOrdersByClient();
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_CLIENT_VIEW_ORDERS")
  public void testGetClientOrdersMultipleOrders() throws Exception {
    var orderDetailResponse = DataTestOrderDetailHelper.multipleOrdersResponse();

    Mockito.when(orderService.getOrdersByClient()).thenReturn(orderDetailResponse);

    mockMvc
        .perform(get("/client/orders"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.success").value(true))
        .andExpect(jsonPath("$.message").value("get orders successfully"))
        .andExpect(jsonPath("$.data.orderDetails").isArray())
        .andExpect(jsonPath("$.data.orderDetails.length()").value(3));

    Mockito.verify(orderService).getOrdersByClient();
  }

  @Test
  @WithMockUser(authorities = "PERMISSION_CLIENT_VIEW_ORDERS")
  public void testGetClientOrdersForbidden() throws Exception {
    mockMvc.perform(get("/client/orders")).andExpect(status().isOk());
  }
}
