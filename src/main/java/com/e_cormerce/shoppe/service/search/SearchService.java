package com.e_cormerce.shoppe.service.search;

import com.e_cormerce.shoppe.dto.common.search.CategoryProjection;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import com.e_cormerce.shoppe.mapper.product.ProductMapper;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.repository.search.KeyWordCategoryRepository;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SearchService {
  KeyWordCategoryRepository keyWordCategoryRepository;

  ProductMapper productMapper;
  private final ProductRepository productRepository;

  public List<CategoryProjection> search(String keyword, int num) {
    return keyWordCategoryRepository.getRelatedCategoryNames(keyword, num);
  }

  public List<ProductCardResponse> searchProductInCategory(String category_id) {

    return productRepository.findProductsInCategory(category_id).stream()
        .map(
            product -> {
              ProductCardResponse response = productMapper.toProductDTO(product);
              return response;
            })
        .toList();
  }
}
