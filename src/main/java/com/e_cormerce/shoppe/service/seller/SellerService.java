package com.e_cormerce.shoppe.service.seller;

import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
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
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerService {
  UploadProductImagesHelper uploadProductImagesHelper;
  ProductService productService;

  public void createProduct(
      CreateProductRequest request,
      MultipartFile thumbnail,
      List<MultipartFile> extraImages,
      List<MultipartFile> variantImages) {

    if (request.getHasVariant()) {
      if (variantImages == null
          || request.getVariantRequests() == null
          || (variantImages.size() != request.getVariantRequests().size()) // sai kích thước
          || (request.getVariantRequests() != null
              && request.getTypes() == null)) // có variant mà ko có type
      {
        throw new AppException(ErrorCode.CONFLICT_VARIANT_DATA);
      }
    } else {
      if (variantImages != null || request.getVariantRequests() != null) {
        throw new AppException(ErrorCode.CONFLICT_VARIANT_DATA);
      }
    }

    ProductImagesUrl urls =
        uploadProductImagesHelper.uploadImagesOfProduct(thumbnail, extraImages, variantImages);

    productService.persistProduct(request, urls);
  }
}
