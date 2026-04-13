package com.e_cormerce.shoppe.controller.seller;

import com.e_cormerce.shoppe.dto.request.order.UpdateOrderTrackingLocationRequest;
import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.order.GetOrderDetailResponse;
import com.e_cormerce.shoppe.dto.response.order.UpdateOrderTrackingLocationResponse;
import com.e_cormerce.shoppe.dto.response.product.MyProductResponse;
import com.e_cormerce.shoppe.dto.response.seller.SellerInfoResponse;
import com.e_cormerce.shoppe.service.order.OrderService;
import com.e_cormerce.shoppe.service.order.OrderTrackingService;
import com.e_cormerce.shoppe.service.seller.SellerService;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerController {
  SellerService sellerService;
  OrderService orderService;
  OrderTrackingService orderTrackingService;

  @GetMapping("/products")
  //    @PreAuthorize("hasAuthority('PERMISSION_VIEW_MY_PRODUCTS')")
  public ResponseEntity<ApiResponse<List<MyProductResponse>>> getAllProducts(
      @PathParam("limit") int limit, @PathParam("offset") int offset) {
    var res = sellerService.getMyProducts(limit, offset);
    return ResponseEntity.status(HttpStatus.OK)
        .body(
            ApiResponse.<List<MyProductResponse>>builder()
                .data(res)
                .success(true)
                .message("get my product successfully")
                .build());
  }

  /**
   * POST: /seller/products. -Tạo sản phẩm với request gửi lên phải có content-type:multipart/
   * form-data.
   *
   * @return
   */
  @PostMapping(path = "/products", consumes = "multipart/form-data")
  @PreAuthorize("hasAuthority('PERMISSION_CREATE_PRODUCT')")
  public ResponseEntity<ApiResponse> create(
      @Valid @RequestPart CreateProductRequest request,
      @RequestPart("thumbnail") MultipartFile thumbnail,
      @RequestPart(value = "extraImages", required = false) List<MultipartFile> extraImages,
      @RequestPart(value = "variantImages", required = false) List<MultipartFile> variantImages) {

    sellerService.createProduct(request, thumbnail, extraImages, variantImages);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(ApiResponse.builder().success(true).message("create product successfully").build());
  }

  @GetMapping("/{id}/info")
  public ResponseEntity<ApiResponse<SellerInfoResponse>> getSeller(@PathVariable String id) {
    var res = sellerService.getSeller(id);
    return ResponseEntity.ok(ApiResponse.<SellerInfoResponse>builder().data(res).build());
  }

  @GetMapping("/{id}/products")
  public ResponseEntity<ApiResponse> getProductsOfSeller(
      @PathVariable String id, @RequestParam Integer limit, @RequestParam Integer offset) {
    var res = sellerService.getProductCardsBySeller(id, limit, offset);
    return ResponseEntity.ok(ApiResponse.builder().data(res).build());
  }

  @PostMapping("/{orderId}/orders/trackings")
  @PreAuthorize("hasAuthority('PERMISSION_UPDATE_ORDER_TRACKING_LOCATION')")
  public ResponseEntity<ApiResponse<UpdateOrderTrackingLocationResponse>> updateTracking(
      @PathVariable String orderId,
      @Valid @RequestBody UpdateOrderTrackingLocationRequest request) {
    var result = orderTrackingService.updateTracking(orderId, request);
    return ResponseEntity.ok(
        ApiResponse.<UpdateOrderTrackingLocationResponse>builder()
            .data(result)
            .message("Tracking updated successfully")
            .success(true)
            .build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_ACCEPT_ORDER')")
  @PatchMapping("/{id}/orders/approve")
  public ResponseEntity<ApiResponse> approveOrder(@PathVariable String id) {
    sellerService.approveOrder(id);
    return ResponseEntity.ok()
        .body(ApiResponse.builder().message("approve order successfully").success(true).build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_CANCEL_ORDER_BY_SELLER')")
  @PatchMapping("/{id}/orders/cancel")
  public ResponseEntity<ApiResponse> cancelOrder(@PathVariable String id) {
    sellerService.cancelOrder(id);
    return ResponseEntity.ok()
        .body(ApiResponse.builder().message("cancel order successfully").success(true).build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_SHIP_ORDER')")
  @PatchMapping("/{id}/orders/ship")
  public ResponseEntity<ApiResponse> shipOrder(@PathVariable String id) {
    sellerService.shipOrder(id);
    return ResponseEntity.ok()
        .body(ApiResponse.builder().message("ship order successfully").success(true).build());
  }

  @GetMapping("/orders")
  @PreAuthorize("hasAuthority('PERMISSION_VIEW_SELLER_ORDERS')")
  public ResponseEntity<ApiResponse<GetOrderDetailResponse>> getSellerOrders() {
    var result = orderService.getOrdersBySeller();
    return ResponseEntity.ok(
        ApiResponse.<GetOrderDetailResponse>builder()
            .data(result)
            .message("get orders successfully")
            .success(true)
            .build());
  }
}
