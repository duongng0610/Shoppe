package com.e_cormerce.shoppe.service.product.helper;

import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.entity.product.Type;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.repository.product.TypeRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class ProductQueryDBHelper {
  TypeRepository typeRepository;
  ProductRepository productRepository;
  VariantRepository variantRepository;

  @Async("queryDBExecutor")
  public CompletableFuture<List<Type>> getTypes(String product_id) {
    return typeRepository.findTypeOfProduct(product_id);
  }

  @Async("queryDBExecutor")
  public CompletableFuture<Category> getCategory(String product_id) {
    return productRepository.findCategoryOfProduct(product_id);
  }

  @Async("queryDBExecutor")
  public CompletableFuture<User> getSeller(String product_id) {
    return productRepository.findSellerOfProduct(product_id);
  }

  @Async("queryDBExecutor")
  public CompletableFuture<List<Variant>> getVariants(String product_id) {
    return variantRepository.findVariantsOfProduct(product_id);
  }
}
