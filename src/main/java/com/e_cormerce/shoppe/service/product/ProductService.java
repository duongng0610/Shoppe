package com.e_cormerce.shoppe.service.product;

import com.e_cormerce.shoppe.dto.request.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.CreateProductResponse;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.product.ProductExtraImage;
import com.e_cormerce.shoppe.enums.ProductStatus;
import com.e_cormerce.shoppe.repository.ProductRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.product.helper.CreateProductHelper;
import com.e_cormerce.shoppe.service.seller.helper.ProductImagesUrl;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
  AuthService authService;
  CreateProductHelper createProductHelper;
  ProductRepository productRepository;

  @Transactional(isolation = Isolation.READ_UNCOMMITTED, timeout = 10)
  public CreateProductResponse persistProduct(
      CreateProductRequest request, ProductImagesUrl urls, boolean hasVariant) {

    Product product =
        Product.builder()
            .name(request.getName())
            .description(request.getReason())
            .originPrice(request.getOriginPrice())
            .status(ProductStatus.PENDING)
            .created_at(LocalDateTime.now())
            .thumbnail(urls.getThumbnailUrl())
            .seller(authService.getUserThroughAuthentication())
            .build();

    if (!urls.getExtraImageUrls().isEmpty()) {
      product.setProductExtraImages(
          urls.getExtraImageUrls().stream()
              .map(url -> ProductExtraImage.builder().product(product).url(url).build())
              .toList());
    }

    if (request.getTypes() != null && !request.getTypes().isEmpty()) {
      product.setTypes(createProductHelper.createType(request.getTypes(), product));
    }

    if (hasVariant) {
      product.setVariants(
          createProductHelper.createVariants(
              request.getVariantRequests(), product, urls.getVariantImageUrls()));
    } else {
      product.setVariants(createProductHelper.createDefaultVariant(product));
    }

    productRepository.save(product);

    return CreateProductResponse.builder()
        .name(request.getName())
        .reason(request.getReason())
        .originPrice(request.getOriginPrice())
        .created_at(LocalDateTime.now())
        .total_quantity(request.getTotalQuantity())
        .types(product.getTypes())
        .variants(product.getVariants())
        .build();
  }
}
