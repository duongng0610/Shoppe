package com.e_cormerce.shoppe.controller.seller;

import com.e_cormerce.shoppe.dto.request.Seller.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.dto.response.CreateProductResponse;
import com.e_cormerce.shoppe.properties.JwtProperties;
import com.e_cormerce.shoppe.service.auth.JwtService;
import com.e_cormerce.shoppe.service.media.CloudinaryService;
import com.e_cormerce.shoppe.service.seller.SellerService;
import com.e_cormerce.shoppe.util.CookieUtil;
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
  CloudinaryService cloudinaryService;
  CookieUtil cookieUtil;
  JwtService jwtService;
  JwtProperties jwtProperties;

  /**
   * POST: /seller/products. -Tạo sản phẩm với request gửi lên phải có content-type:multipart/
   * form-data.
   *
   * @param product
   * @param thumbnail
   * @param hasExtraImages
   * @param extraImages
   * @param hasVariant
   * @param variantImages
   * @return
   */
  @PostMapping(path = "/product", consumes = "multipart/form-data")
  @PreAuthorize("hasRole('SELLER') or hasPermissions('CREATE_PRODUCT')")
  public ResponseEntity<ApiResponse<CreateProductResponse>> create(
      @RequestPart CreateProductRequest product,
      @RequestPart MultipartFile thumbnail,
      @RequestPart boolean hasExtraImages,
      @RequestParam List<MultipartFile> extraImages,
      @RequestPart boolean hasVariant,
      @RequestParam List<MultipartFile> variantImages) {
    var result =
        sellerService.createProduct(
            product, thumbnail, hasExtraImages, extraImages, hasVariant, variantImages);
    return ResponseEntity.status(HttpStatus.CREATED)
        .body(
            ApiResponse.<CreateProductResponse>builder()
                .data(result)
                .success(true)
                .message("create product successfully")
                .build());
  }
}
