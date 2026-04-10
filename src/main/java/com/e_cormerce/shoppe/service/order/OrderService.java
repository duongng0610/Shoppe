package com.e_cormerce.shoppe.service.order;

import com.e_cormerce.shoppe.dto.request.order.CreateOrderRequest;
import com.e_cormerce.shoppe.dto.response.order.CreateOrderResponse;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.Address;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.event.OrderCancelledEvent;
import com.e_cormerce.shoppe.event.OrderCreatedEvent;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.service.order.helper.CreateOrderHelper;
import com.e_cormerce.shoppe.service.vnpay.VnPayService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderService {
    CreateOrderHelper createOrderHelper;
    OrderRepository orderRepository;
    ApplicationEventPublisher eventPublisher;
    VnPayService vnPayService;

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
                        .thumbnail(variant.getThumbnail())
                        .shippingPhoneNumber(request.getShippingPhoneNumber())
                        .totalPrice(createOrderHelper.getTotalPrice(variant, request.getQuantity()))
                        .build();

        createOrderHelper.getOrderInfo(order, variant);

        orderRepository.save(order);

        eventPublisher.publishEvent(
                OrderCreatedEvent.builder()
                        .orderId(order.getId())
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

    @Transactional
    public void cancelOrder(String orderId) {
        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));

        Variant variant = order.getVariant();
        int availableQuantity = variant.getQuantity();
        int orderQuantity = order.getQuantity();

        variant.setQuantity(availableQuantity + orderQuantity);

        order.setStatus(OrderStatus.CANCELLED_BY_CLIENT);
        variant.setQuantity(availableQuantity + orderQuantity);

        eventPublisher.publishEvent(
                OrderCancelledEvent.builder()
                        .orderId(orderId)
                        .orderStatus(OrderStatus.CANCELLED_BY_CLIENT.toString())
                        .client(order.getClient())
                        .variant(variant)
                        .build());
    }

    public String getUrlPaymentByOrder(String id, HttpServletRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));
        if (!userId.equals(orderRepository.getClientId(id).orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER)))) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(order.getTotalPrice().intValue(), order.getId(), userId, "Thanh toán đơn hàng " + id, baseUrl);
        return vnpayUrl;
    }
}
