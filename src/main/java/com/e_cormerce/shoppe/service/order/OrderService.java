package com.e_cormerce.shoppe.service.order;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import com.e_cormerce.shoppe.dto.common.order.OrderDetailDto;
import com.e_cormerce.shoppe.dto.request.order.CreateOrderRequest;
import com.e_cormerce.shoppe.dto.response.order.CreateOrderResponse;
import com.e_cormerce.shoppe.dto.response.order.GetOrderDetailResponse;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.enums.product.ProductStatus;
import com.e_cormerce.shoppe.event.order.OrderCancelledByClient;
import com.e_cormerce.shoppe.event.order.OrderCreated;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.order.OrderMapper;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.order.helper.CreateOrderHelper;
import com.e_cormerce.shoppe.service.vnpay.VnPayService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
    ObjectMapper objectMapper;
    UserMapper userMapper;
    AuthService authService;

    @Transactional
    public CreateOrderResponse create(@Valid CreateOrderRequest request) throws JsonProcessingException {
        Variant variant = createOrderHelper.getVariant(request.getVariantId());
        Product product = variant.getProduct();
        User seller = product.getSeller();
        User client = authService.getUserThroughAuthentication();
        AddressDto address = request.getAddress();
        String phoneNumber = request.getShippingPhoneNumber();
        int orderedQuantity = request.getQuantity();
        if (product.getStatus() != ProductStatus.ACTIVE) {
            throw new AppException(ErrorCode.PRODUCT_NOT_ACTIVE);
        }
        Order order =
                Order.builder()
                        .quantity(request.getQuantity())
                        .variant(variant)
                        .status(OrderStatus.PENDING)
                        .client(client)
                        .seller(seller)
                        .variantThumbnail(variant.getThumbnail())
                        .province(address.getProvince())
                        .district(address.getDistrict())
                        .ward(address.getWard())
                        .shippingPhoneNumber(phoneNumber)
                        .totalPrice(createOrderHelper.getTotalPrice(variant, request.getQuantity()))
                        .productName(product.getName())
                        .variantAttributes(variant.isDefault() ? null : createOrderHelper.getAttributesVariant(variant))
                        .createdAt(LocalDateTime.now())
                        .build();


        orderRepository.save(order);

        eventPublisher.publishEvent(
                OrderCreated.builder()
                        .order(orderMapper.toOrderDto(order))
                        .client(userMapper.toDto(client))
                        .seller(userMapper.toDto(seller))
                        .productId(product.getId())
                        .variantId(variant.getId())
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

    @Transactional
    public void approveOrder(String orderId) {
        Order order =
                orderRepository
                        .findById(orderId)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new AppException(ErrorCode.UNABLE_HIDDEN_PRODUCT);
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
    public GetOrderDetailResponse getOrdersByClient() {
        String clientId = createOrderHelper.getUserId();

        List<OrderDetailDto> orderDetails =
                orderRepository.findOrderOfClient(clientId).stream()
                        .map(orderMapper::toOrderDetailResponse)
                        .collect(Collectors.toList());

        return GetOrderDetailResponse.builder().orderDetails(orderDetails).build();
    }

    @Transactional(readOnly = true)
    public GetOrderDetailResponse getOrdersBySeller() {
        String sellerId = createOrderHelper.getUserId();

        List<OrderDetailDto> orderDetails =
                orderRepository.findOrderOfSeller(sellerId).stream()
                        .map(orderMapper::toOrderDetailResponse)
                        .collect(Collectors.toList());

        return GetOrderDetailResponse.builder().orderDetails(orderDetails).build();
    }

    public String getUrlPaymentByOrder(String id, HttpServletRequest request) {
        String userId = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
        Order order = orderRepository.findById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_ORDER));
        if (!userId.equals(order.getClient().getId())) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createUrlPayment(order.getTotalPrice().intValue(), order.getId(), userId, "Thanh toán đơn hàng " + id, baseUrl);
        return vnpayUrl;
    }
}
