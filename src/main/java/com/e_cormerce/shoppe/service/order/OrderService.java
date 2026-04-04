package com.e_cormerce.shoppe.service.order;

import com.e_cormerce.shoppe.dto.request.order.CreateOrderRequest;
import com.e_cormerce.shoppe.dto.response.order.CreateOrderResponse;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.Address;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.event.OrderCreatedEvent;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.service.order.helper.CreateOrderHelper;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
  CreateOrderHelper createOrderHelper;
  OrderRepository orderRepository;
  ApplicationEventPublisher eventPublisher;

  @Transactional
  public CreateOrderResponse create(@Valid CreateOrderRequest request) {
    Variant variant = createOrderHelper.getVariant(request.getVariantId());
    User seller = variant.getProduct().getSeller();
    User client = createOrderHelper.getClient();
    Address shippingAddress = createOrderHelper.getShippingAddress(request.getShippingAddress());
    if (variant.getQuantity() < request.getQuantity()) {
      throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
    }
    Order order =
        Order.builder()
            .quantity(request.getQuantity())
            .variant(variant)
            .status(OrderStatus.PENDING)
            .client(client)
            .seller(seller)
            .shippingAddress(shippingAddress)
            .shippingPhoneNumber(request.getShippingPhoneNumber())
            .totalPrice(createOrderHelper.getTotalPrice(variant, request.getQuantity()))
            .build();

    createOrderHelper.getOrderInfo(order, variant);

    orderRepository.save(order);

    eventPublisher.publishEvent(
        OrderCreatedEvent.builder()
            .order(order)
            .client(client)
            .seller(seller)
            .variant(variant)
            .build());

    return CreateOrderResponse.builder()
        .orderId(order.getId())
        .clientId(client.getId())
        .sellerId(seller.getId())
        .build();
  }
}
