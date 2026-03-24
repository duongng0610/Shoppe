package com.e_cormerce.shoppe.service.category.helper;

import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.product.CategoryRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryHelper {

    CategoryRepository categoryRepository;

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
