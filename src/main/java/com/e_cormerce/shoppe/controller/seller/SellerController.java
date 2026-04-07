package com.e_cormerce.shoppe.controller.seller;

import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.seller.SellerInfoResponse;
import com.e_cormerce.shoppe.service.order.OrderService;
import com.e_cormerce.shoppe.service.seller.SellerService;
import jakarta.validation.Valid;
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

  @PreAuthorize("hasAuthority('PERMISSION_ACCEPT_ORDER')")
  @PatchMapping("/{id}/order/approve")
  public ResponseEntity<ApiResponse> approveOrder(@PathVariable String id) {
    sellerService.approveOrder(id);
    return ResponseEntity.ok()
        .body(ApiResponse.builder().message("approve order successfully").success(true).build());
  }

  @PreAuthorize("hasAuthority('PERMISSION_CANCEL_ORDER_BY_SELLER')")
  @PatchMapping("/{id}/order/cancel")
  public ResponseEntity<ApiResponse> cancelOrder(@PathVariable String id) {
    sellerService.cancelOrder(id);
    return ResponseEntity.ok()
        .body(ApiResponse.builder().message("cancel order successfully").success(true).build());
  }
}
