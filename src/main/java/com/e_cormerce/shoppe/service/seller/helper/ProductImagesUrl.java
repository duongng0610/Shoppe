package com.e_cormerce.shoppe.service.seller.helper;

import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductImagesUrl {

  String thumbnailUrl;
  List<String> extraImageUrls;
  List<String> variantImageUrls;

  public ProductImagesUrl(
      String thumbnailUrl, List<String> extraImageUrls, List<String> variantImageUrls) {

    if (thumbnailUrl == null || thumbnailUrl.isBlank()) {
      throw new IllegalArgumentException("thumbnailUrl cannot be null or blank");
    }

    this.thumbnailUrl = thumbnailUrl;

    this.extraImageUrls = extraImageUrls != null ? List.copyOf(extraImageUrls) : List.of();

    this.variantImageUrls = variantImageUrls != null ? List.copyOf(variantImageUrls) : List.of();
  }

  public static ProductImagesUrl thumbnailOnly(String thumbnailUrl) {
    return new ProductImagesUrl(thumbnailUrl, List.of(), List.of());
  }
}
