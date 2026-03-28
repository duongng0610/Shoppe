package com.e_cormerce.shoppe.service.order;

import com.e_cormerce.shoppe.dto.request.order.CreateOrderRequest;
import com.e_cormerce.shoppe.dto.response.order.CreateOrderResponse;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
  CreateOrderHelper createOrderHelper;
  OrderRepository orderRepository;

  public CreateOrderResponse create(@Valid CreateOrderRequest request) {
    Variant variant = createOrderHelper.getVariant(request.getVariantId());
    User seller = createOrderHelper.getSeller(request.getSellerId());
    User client = createOrderHelper.getClient();

    Order order =
        Order.builder()
            .quantity(request.getQuantity())
            .variant(variant)
            .status(OrderStatus.PENDING)
            .client(client)
            .seller(seller)
            .shippingAddress(createOrderHelper.getShippingAddress(request.getShippingAddress()))
            .shippingPhoneNumber(request.getShippingPhoneNumber())
            .totalPrice(createOrderHelper.getTotalPrice(variant, request.getQuantity()))
            .build();

    createOrderHelper.getOrderInfo(order, variant);

    orderRepository.save(order);

    return CreateOrderResponse.builder()
        .orderId(order.getId())
        .clientId(client.getId())
        .sellerId(seller.getId())
        .build();
  }
}
