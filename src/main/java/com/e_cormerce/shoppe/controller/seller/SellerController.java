package com.e_cormerce.shoppe.controller.seller;

import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.service.seller.SellerService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/seller")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerController {
    SellerService sellerService;

    /**
     * POST: /seller/products. -Tạo sản phẩm với request gửi lên phải có content-type:multipart/
     * form-data.
     *
     * @return
     */
    @PostMapping(path = "/product", consumes = "multipart/form-data")
    @PreAuthorize("hasAuthority('PERMISSION_CREATE_PRODUCT')")
    public ResponseEntity<ApiResponse> create(
            @Valid @RequestPart CreateProductRequest request, @RequestPart("thumbnail") MultipartFile thumbnail
            , @RequestPart(value = "extraImages", required = false) List<MultipartFile> extraImages, @RequestPart(value = "variantImages", required = false) List<MultipartFile> variantImages) {
        if (request.getHasVariant()) {
            if (variantImages == null
                    || request.getVariantRequests() == null
                    || (variantImages.size() != request.getVariantRequests().size())//sai kích thước
                    || (request.getVariantRequests() != null && request.getTypes() == null)) //có variant mà ko có type
            {
                throw new AppException(ErrorCode.CONFLICT_VARIANT_DATA);
            }
        } else {
            if (variantImages != null || request.getVariantRequests() != null) {
                throw new AppException(ErrorCode.CONFLICT_VARIANT_DATA);
            }
        }

        sellerService.createProduct(request, thumbnail, extraImages, variantImages);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(
                        ApiResponse.builder()
                                .success(true)
                                .message("create product successfully")
                                .build());
    }
}
