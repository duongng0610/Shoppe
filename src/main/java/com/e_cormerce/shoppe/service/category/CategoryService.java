package com.e_cormerce.shoppe.service.category;

import com.e_cormerce.shoppe.dto.request.admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.entity.search.KeyWordCategory;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.product.CategoryRepository;
import com.e_cormerce.shoppe.repository.search.KeyWordCategoryRepository;
import com.e_cormerce.shoppe.service.category.helper.CreateCategoryHelper;
import com.e_cormerce.shoppe.service.media.ImageService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

  CreateCategoryHelper createCategoryHelper;
  ImageService imageService;
  CategoryRepository categoryRepository;
  KeyWordCategoryRepository keyWordCategoryRepository;

  public void create(CreateCategoryRequest request, MultipartFile thumbnail) {
    if (categoryRepository.existsByVal(request.getName())) {
      throw new AppException(ErrorCode.EXISTED_CATEGORY);
    }
    Category category =
        Category.builder()
            .val(request.getName().toLowerCase())
            .thumbnail(imageService.uploadSingleImage(thumbnail))
            .build();
    /** nếu khác null thì mơi theem parent , còn ko thì vẫn tạo với mức mặc định. */
    if (request.getParentId() != null) {
      category.setParent(createCategoryHelper.findById(request.getParentId()));
    }

    if (request.getSynonyms() != null) {
      request
          .getSynonyms()
          .forEach(
              relevant -> {
                keyWordCategoryRepository.save(
                    KeyWordCategory.builder()
                        .category_id(category.getId())
                        .keyword(relevant)
                        .build());
              });
    }
    keyWordCategoryRepository.save(
        KeyWordCategory.builder().category_id(category.getId()).keyword(request.getName()).build());
    categoryRepository.save(category);
  }

  public List<Category> getChildren(String id) {
    List<Category> children = createCategoryHelper.findChildren(id);
    return children;
  }

  public List<Category> getDefault() {
    List<Category> defaults = createCategoryHelper.findDefault();
    return defaults;
  }
}
