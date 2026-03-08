package com.e_cormerce.shoppe.service.seller;

import com.e_cormerce.shoppe.dto.request.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.CreateProductResponse;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.service.product.ProductService;
import com.e_cormerce.shoppe.service.seller.helper.ProductImagesUrl;
import com.e_cormerce.shoppe.service.seller.helper.UploadProductImagesHelper;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerService {
  UploadProductImagesHelper uploadProductImagesHelper;
  ProductService productService;

  @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 10)
  public CreateProductResponse createProduct(
      CreateProductRequest request,
      MultipartFile thumbnail,
      boolean hasExtraImages,
      List<MultipartFile> extraImages,
      boolean hasVariant,
      List<MultipartFile> variantImages) {

    if (hasVariant
        && variantImages != null
        && variantImages.size() != request.getVariantRequests().size()) {
      throw new AppException(ErrorCode.INVALID_CREATE_VARIANTS);
    }

    ProductImagesUrl urls =
        uploadProductImagesHelper.uploadImagesOfProduct(thumbnail, extraImages, variantImages);

    return productService.persistProduct(request, urls, hasVariant);
  }
}
