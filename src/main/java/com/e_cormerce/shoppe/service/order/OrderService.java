package com.e_cormerce.shoppe.service.order;

import com.e_cormerce.shoppe.dto.request.order.CreateOrderRequest;
import com.e_cormerce.shoppe.dto.response.ghn.order_ship.info.GhnOrderShipInfoDataResponse;
import com.e_cormerce.shoppe.dto.response.order.CreateOrderResponse;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.order.OrderPaymentStatus;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.enums.transaction.TransactionStatus;
import com.e_cormerce.shoppe.event.order.OrderCancelledByClient;
import com.e_cormerce.shoppe.event.order.OrderCreated;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.order.OrderMapper;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.projection.order.OrderRevenueProjection;
import com.e_cormerce.shoppe.projection.user.OrderWithUserInfoProjection;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.repository.order.OrderReservationRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.order.helper.CreateOrderHelper;
import com.e_cormerce.shoppe.service.ship.ShippingService;
import com.e_cormerce.shoppe.service.vnpay.VnPayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
  CreateOrderHelper createOrderHelper;
  OrderRepository orderRepository;
  OrderMapper orderMapper;
  ApplicationEventPublisher eventPublisher;
  VnPayService vnPayService;
  VariantRepository variantRepository;
  UserMapper userMapper;
  OrderReservationRepository orderReservationRepository;
  AuthService authService;
  ShippingService shippingService;

  @Transactional
  public CreateOrderResponse create(
      @Valid CreateOrderRequest request, HttpServletRequest httpServletRequest)
      throws JsonProcessingException {
    Variant variant = variantRepository.getVariantWithProductAndSeller(request.getVariantId());
    Product product = variant.getProduct();

    int orderedQuantity = request.getQuantity();
    // step1
    createOrderHelper.checkQuantityAndActiveProduct(variant, orderedQuantity);

    var order = createOrderHelper.mainExecute(variant, request);
    var client = order.getClient();
    var seller = order.getSeller();

    // publish event
    eventPublisher.publishEvent(
        OrderCreated.builder()
            .order(orderMapper.toOrderDto(order))
            .client(userMapper.toDto(client))
            .seller(userMapper.toDto(seller))
            .productId(product.getId())
            .variantId(variant.getId())
            .build());

    return CreateOrderResponse.builder()
        .order(orderMapper.toOrderDto(order))
        .sellerId(seller.getId())
        .clientId(client.getId())
        .paymentUrl(getUrlPaymentByOrder(order.getId(), httpServletRequest))
        .build();
  }

  @Transactional
  public void cancelOrder(String orderId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));

    if (order.getStatus() != OrderStatus.PENDING) {
      throw new AppException(ErrorCode.UNABlE_CANCEL_ORDER);
    }

    Variant variant = order.getVariant();
    int availableQuantity = variant.getQuantity();
    int orderQuantity = order.getQuantity();

    variant.setQuantity(availableQuantity + orderQuantity);

    order.setStatus(OrderStatus.CANCELLED_BY_CLIENT);
    variant.setQuantity(availableQuantity + orderQuantity);

    eventPublisher.publishEvent(
        OrderCancelledByClient.builder()
            .order(orderMapper.toOrderDto(order))
            .client(userMapper.toDto(order.getClient()))
            .seller(userMapper.toDto(order.getSeller()))
            .build());
  }

  @Transactional(readOnly = true)
  public List<OrderWithUserInfoProjection> getOrdersByClient(int limit, int offset) {
    String clientId = createOrderHelper.getUserId();
    return orderRepository.findOrdersByClientId(clientId, limit, offset);
  }

  @Transactional(readOnly = true)
  public List<OrderWithUserInfoProjection> getOrderByAdmin(Integer limit, Integer offset) {

    return orderRepository.findOrderSystem(limit, offset);
  }

  @Transactional(readOnly = true)
  public List<OrderWithUserInfoProjection> getSystemOrder(Integer limit, Integer offset) {
    String sellerId = authService.getUserId();
    return orderRepository.findOrdersBySellerId(sellerId, limit, offset);
  }

  public String getUrlPaymentByOrder(String id, HttpServletRequest request) {
    String userId =
        SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
    Order order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));
    if (!userId.equals(order.getClient().getId())) {
      throw new AppException(ErrorCode.UNAUTHORIZED);
    }
    String baseUrl =
        request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

    String vnpayUrl =
        vnPayService.createUrlPayment(
            order.getTotalPrice().intValue(),
            order.getId(),
            userId,
            "Thanh toán đơn hàng " + id,
            baseUrl);
    return vnpayUrl;
  }

  @Transactional(propagation = Propagation.REQUIRED)
  public void handleVnpayResult(Order order, User user, TransactionStatus status) {

    if (status == com.e_cormerce.shoppe.enums.transaction.TransactionStatus.SUCCESS) {
      order.setPaymentStatus(OrderPaymentStatus.SUCCESS);
    } else {
      order.setPaymentStatus(OrderPaymentStatus.FAIL);
    }
    orderRepository.save(order);
  }

  @Transactional
  public void updateDeliverOrder(String id, OrderStatus orderStatus) {
    Order order =
        orderRepository
            .findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));
    if (order.getStatus() != OrderStatus.SHIPPING) {
      throw new AppException(ErrorCode.UNABLE_UPDATE_ORDER_DELIVERY_STATE);
    }
    order.setStatus(orderStatus);
    orderRepository.save(order);
  }

  // =====================================
  // SELLER
  // =====================================

  public OrderRevenueProjection getSellerRevenue(String sellerId, String status, Integer days) {

    return orderRepository.getSellerRevenue(sellerId, status, days);
  }

  @Cacheable(value = "order_ships", key = "#orderId")
  public GhnOrderShipInfoDataResponse getOrderShipInfo(String orderId) {
    Order order =
        orderRepository
            .findById(orderId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));
    //        String userId = authService.getUserId();
    //        if (!order.getSeller().getId().equals(userId) &&
    // !order.getClient().getId().equals(userId)) {
    //            throw new AppException(ErrorCode.UNAUTHORIZED);
    //        }
    if (order.getStatus() != OrderStatus.SHIPPING) {
      throw new AppException(ErrorCode.UNABLE_VIEW_ORDER_SHIPPING);
    }
    return shippingService.getShipInfo(orderId);
  }

  // =====================================
  // ADMIN
  // =====================================

  public OrderRevenueProjection getAdminRevenue(String status, Integer days) {

    return orderRepository.getAdminRevenue(status, days);
  }
}
