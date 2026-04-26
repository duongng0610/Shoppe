package com.e_cormerce.shoppe.service.media.validator;

import com.e_cormerce.shoppe.enums.media.ImageType;
import com.e_cormerce.shoppe.repository.product.ProductExtraImageRepository;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImageValidator implements ImageValidator {

  ProductRepository productRepository;
  VariantRepository variantRepository;
  ProductExtraImageRepository productExtraImageRepository;

  @Override
  public ImageType getSupportedType() {
    return ImageType.PRODUCT;
  }

  @Override
  public boolean isUsed(String imageId) {
    return productRepository.existsByThumbnailId(imageId)
        || variantRepository.existsByThumbnailId(imageId)
        || productExtraImageRepository.existsByImageId(imageId);
  }
}
