package com.e_cormerce.shoppe.service.seller;

import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
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

    ProductImagesUrl urls =
        uploadProductImagesHelper.uploadImagesOfProduct(thumbnail, extraImages, variantImages);

    productService.persistProduct(request, urls);
  }
}
