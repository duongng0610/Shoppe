package com.e_cormerce.shoppe.event.listener.category;

import com.e_cormerce.shoppe.event.catgory.CategorySearched;
import com.e_cormerce.shoppe.event.product.ProductCreated;
import com.e_cormerce.shoppe.repository.analytic.CategoryDailyRepository;
import com.e_cormerce.shoppe.repository.catgory.SynonymsRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryListener {
  CategoryDailyRepository categoryDailyRepository;
  SynonymsRepository synonymsRepository;

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleProductCreated(ProductCreated event) {
    var product = event.getProduct();
    var category = event.getCategory();
    categoryDailyRepository.increaseNewProduct(
        category.getId(), product.getCreatedAt().toLocalDate());
  }

  @Async
  @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
  public void handleCategorySearched(CategorySearched event) {
    var category = event.getCategory();
    categoryDailyRepository.increaseSearch(category.getId(), event.getCreatedAt().toLocalDate());
  }
}
