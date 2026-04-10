package com.e_cormerce.shoppe.controller.client.order;

import com.e_cormerce.shoppe.dto.common.order.OrderDetailDto;
import com.e_cormerce.shoppe.dto.response.order.GetOrderDetailResponse;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class DataTestOrderDetailHelper {

  /** Creates a valid GetOrderDetailResponse with sample order details */
  public static GetOrderDetailResponse validGetOrderDetailResponse() {
    List<OrderDetailDto> orderDetails = new ArrayList<>();
    orderDetails.add(
        OrderDetailDto.builder()
            .id("order-001")
            .productName("Áo Thun Cotton")
            .variantThumbnail(
                "https://res.cloudinary.com/dhwxpgxzl/image/upload/v1775792912/xabc63tyhzfxos48lnia.png")
            .clientUsername("client1")
            .sellerUsername("seller1")
            .quantity(1)
            .totalPrice(BigDecimal.valueOf(150.00))
            .status("COMPLETED")
            .shippingPhoneNumber("0123456789")
            .createdAt(LocalDateTime.now().minusDays(5))
            .build());

    return GetOrderDetailResponse.builder().orderDetails(orderDetails).build();
  }

  /** Creates an empty GetOrderDetailResponse with no orders */
  public static GetOrderDetailResponse emptyGetOrderDetailResponse() {
    return GetOrderDetailResponse.builder().orderDetails(new ArrayList<>()).build();
  }

  /** Creates a GetOrderDetailResponse with multiple orders */
  public static GetOrderDetailResponse multipleOrdersResponse() {
    List<OrderDetailDto> orderDetails = new ArrayList<>();

    orderDetails.add(
        OrderDetailDto.builder()
            .id("order-001")
            .productName("Áo Thun Cotton")
            .variantThumbnail(
                "https://res.cloudinary.com/dhwxpgxzl/image/upload/v1775792912/xabc63tyhzfxos48lnia.png")
            .clientUsername("client1")
            .sellerUsername("seller1")
            .quantity(1)
            .totalPrice(BigDecimal.valueOf(150.00))
            .status("COMPLETED")
            .shippingPhoneNumber("0123456789")
            .createdAt(LocalDateTime.now().minusDays(5))
            .build());

    orderDetails.add(
        OrderDetailDto.builder()
            .id("order-002")
            .productName("Quần Jean Nam")
            .variantThumbnail(
                "https://res.cloudinary.com/dhwxpgxzl/image/upload/v1775792912/abc123def456.png")
            .clientUsername("client2")
            .sellerUsername("seller2")
            .quantity(2)
            .totalPrice(BigDecimal.valueOf(250.50))
            .status("PENDING")
            .shippingPhoneNumber("0987654321")
            .createdAt(LocalDateTime.now().minusDays(2))
            .build());

    orderDetails.add(
        OrderDetailDto.builder()
            .id("order-003")
            .productName("Giày Thể Thao")
            .variantThumbnail(
                "https://res.cloudinary.com/dhwxpgxzl/image/upload/v1775792912/xyz789uvw012.png")
            .clientUsername("client3")
            .sellerUsername("seller1")
            .quantity(1)
            .totalPrice(BigDecimal.valueOf(75.25))
            .status("SHIPPED")
            .shippingPhoneNumber("0123123123")
            .createdAt(LocalDateTime.now().minusHours(24))
            .build());

    return GetOrderDetailResponse.builder().orderDetails(orderDetails).build();
  }

  /** Creates a GetOrderDetailResponse with single order */
  public static GetOrderDetailResponse singleOrderResponse() {
    List<OrderDetailDto> orderDetails = new ArrayList<>();
    orderDetails.add(
        OrderDetailDto.builder()
            .id("order-100")
            .productName("Áo Sơ Mi Nam")
            .variantThumbnail(
                "https://res.cloudinary.com/dhwxpgxzl/image/upload/v1775792912/shirt001.png")
            .clientUsername("client5")
            .sellerUsername("seller3")
            .quantity(1)
            .totalPrice(BigDecimal.valueOf(500.00))
            .status("COMPLETED")
            .shippingPhoneNumber("0111111111")
            .createdAt(LocalDateTime.now().minusDays(10))
            .build());

    return GetOrderDetailResponse.builder().orderDetails(orderDetails).build();
  }

  /** Creates a GetOrderDetailResponse with high-value orders */
  public static GetOrderDetailResponse highValueOrdersResponse() {
    List<OrderDetailDto> orderDetails = new ArrayList<>();

    orderDetails.add(
        OrderDetailDto.builder()
            .id("order-premium-001")
            .productName("Laptop Dell XPS")
            .variantThumbnail(
                "https://res.cloudinary.com/dhwxpgxzl/image/upload/v1775792912/laptop001.png")
            .clientUsername("vip_client1")
            .sellerUsername("premium_seller1")
            .quantity(1)
            .totalPrice(BigDecimal.valueOf(1500.00))
            .status("COMPLETED")
            .shippingPhoneNumber("0900000001")
            .createdAt(LocalDateTime.now().minusDays(15))
            .build());

    orderDetails.add(
        OrderDetailDto.builder()
            .id("order-premium-002")
            .productName("iPhone 15 Pro")
            .variantThumbnail(
                "https://res.cloudinary.com/dhwxpgxzl/image/upload/v1775792912/iphone001.png")
            .clientUsername("vip_client2")
            .sellerUsername("premium_seller2")
            .quantity(1)
            .totalPrice(BigDecimal.valueOf(2500.75))
            .status("SHIPPED")
            .shippingPhoneNumber("0900000002")
            .createdAt(LocalDateTime.now().minusDays(3))
            .build());

    return GetOrderDetailResponse.builder().orderDetails(orderDetails).build();
  }

  /** Creates a GetOrderDetailResponse with cancelled orders */
  public static GetOrderDetailResponse cancelledOrdersResponse() {
    List<OrderDetailDto> orderDetails = new ArrayList<>();

    orderDetails.add(
        OrderDetailDto.builder()
            .id("order-cancel-001")
            .productName("Áo Lỗi Size")
            .variantThumbnail(
                "https://res.cloudinary.com/dhwxpgxzl/image/upload/v1775792912/cancel001.png")
            .clientUsername("cancel_client1")
            .sellerUsername("seller_cancel1")
            .quantity(1)
            .totalPrice(BigDecimal.valueOf(100.00))
            .status("CANCELLED")
            .shippingPhoneNumber("0888888888")
            .createdAt(LocalDateTime.now().minusDays(20))
            .build());

    orderDetails.add(
        OrderDetailDto.builder()
            .id("order-cancel-002")
            .productName("Quần Lỗi Màu")
            .variantThumbnail(
                "https://res.cloudinary.com/dhwxpgxzl/image/upload/v1775792912/cancel002.png")
            .clientUsername("cancel_client2")
            .sellerUsername("seller_cancel2")
            .quantity(2)
            .totalPrice(BigDecimal.valueOf(200.00))
            .status("CANCELLED")
            .shippingPhoneNumber("0877777777")
            .createdAt(LocalDateTime.now().minusDays(7))
            .build());

    return GetOrderDetailResponse.builder().orderDetails(orderDetails).build();
  }
}
