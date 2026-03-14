package com.e_cormerce.shoppe.service.category.helper;

import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.CategoryRepository;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateCategoryHelper {

  CategoryRepository categoryRepository;

  @Async("queryDBExecutor")
  public CompletableFuture<Category> findParent(String parentId) {
    Category category =
        categoryRepository
            .findById(parentId)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_PARENT_CATEGORY));

    return CompletableFuture.completedFuture(category);
  }

  public Category findById(String id) {
    Category category =
        categoryRepository
            .findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.INVALID_PARENT_CATEGORY));

    return category;
  }

  public List<Category> findDefault() {
    return categoryRepository.findDefault();
  }

  public List<Category> findChildren(String id) {
    return categoryRepository.findChildren(id);
  }
}
