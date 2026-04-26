package com.e_cormerce.shoppe.service.order.helper;

import com.e_cormerce.shoppe.dto.common.address.AddressDto;
import com.e_cormerce.shoppe.dto.common.product.VariantAttributeDto;
import com.e_cormerce.shoppe.dto.request.order.CreateOrderRequest;
import com.e_cormerce.shoppe.dto.request.order.ShipCostOrderRequest;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.order.OrderReservation;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.order.OrderPaymentStatus;
import com.e_cormerce.shoppe.enums.order.OrderStatus;
import com.e_cormerce.shoppe.enums.order.ReservationStatus;
import com.e_cormerce.shoppe.enums.product.ProductStatus;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.repository.order.OrderReservationRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.repository.user.UserRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.ship.ShippingService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class CreateOrderHelper {
  VariantRepository variantRepository;
  UserRepository userRepository;
  AuthService authService;
  ObjectMapper objectMapper;
  OrderRepository orderRepository;
  OrderReservationRepository orderReservationRepository;
  ShippingService shippingService;

  public Variant getVariant(@NotBlank String variantId) {
    return variantRepository
        .findById(variantId)
        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_VARIANT));
  }

  public User getSeller(@NotBlank String sellerId) {
    return userRepository
        .findById(sellerId)
        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_USER));
  }

  public User getClient() {
    return authService.getUserThroughAuthentication();
  }

  public BigDecimal getTotalPrice(Variant variant, int quantity, BigDecimal shipCost) {
    BigDecimal totalPrice = variant.getPrice().multiply(BigDecimal.valueOf(quantity));
    return totalPrice.add(shipCost);
  }

  public String getAttributesVariant(Variant variant) {
    try {
      return objectMapper.writeValueAsString(
          variant.getVariantValues().stream()
              .map(
                  variantValue ->
                      VariantAttributeDto.builder()
                          .name(variantValue.getValue().getType().getVal())
                          .value(variantValue.getValue().getVal())
                          .build()));
    } catch (JsonProcessingException e) {
      throw new RuntimeException(e);
    }
  }

  public void checkTotalPrice(Variant variant, BigDecimal totalPrice) {}

  public String getUserId() {
    return authService.getUserId();
  }

  public void checkQuantityAndActiveProduct(Variant variant, int orderedQuantity) {
    // step1: check active product
    if (variant.getProduct().getStatus() != ProductStatus.ACTIVE) {
      throw new AppException(ErrorCode.PRODUCT_NOT_ACTIVE);
    }
    // step2: check quantity
    if (variant.getQuantity() < orderedQuantity) {
      throw new AppException(ErrorCode.INSUFFICIENT_STOCK);
    }
  }

  @Transactional()
  public Order mainExecute(Variant variant, CreateOrderRequest request) {
    Product product = variant.getProduct();
    User seller = product.getSeller();
    User client = authService.getUserThroughAuthentication();
    AddressDto address = request.getAddress();
    String phoneNumber = request.getShippingPhoneNumber();
    int orderedQuantity = request.getQuantity();
    int affectedRows = variantRepository.reservedStock(variant.getId(), orderedQuantity);

    if (affectedRows == 0) {
      throw new AppException(ErrorCode.NOT_RESERVE_AVAILABLE);
    }

    var shipCost =
        BigDecimal.valueOf(
            shippingService
                .getShipCost(
                    ShipCostOrderRequest.builder()
                        .address(address)
                        .sellerId(seller.getId())
                        .build())
                .getTotal());

    var order =
        Order.builder()
            .quantity(orderedQuantity)
            .variant(variant)
            .status(OrderStatus.PENDING)
            .paymentStatus(OrderPaymentStatus.PENDING)
            .client(client)
            .seller(seller)
            .orderThumbnail(variant.getThumbnail())
            .orderName(product.getName())
            .province(address.getProvince())
            .district(address.getDistrict())
            .ward(address.getWard())
            .shippingPhoneNumber(phoneNumber)
            .priceEach(variant.getPrice())
            .shipCost(shipCost)
            .totalPrice(getTotalPrice(variant, orderedQuantity, shipCost))
            .variantAttributes(variant.isDefault() ? null : getAttributesVariant(variant))
            .createdAt(LocalDateTime.now())
            .build();

    orderRepository.save(order);

    var newReservation =
        OrderReservation.builder()
            .order(order)
            .status(ReservationStatus.ACTIVE)
            .variant(variant)
            .quantity(orderedQuantity)
            .expireAt(LocalDateTime.now().plusMinutes(15))
            .build();

    orderReservationRepository.save(newReservation);

    return order;
  }
}
