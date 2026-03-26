package com.e_cormerce.shoppe.service.category;

import com.e_cormerce.shoppe.dto.request.admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.response.category.CategoryDetailResponse;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.category.CategorySynonyms;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.product.ProductMapper;
import com.e_cormerce.shoppe.repository.catgory.CategoryRepository;
import com.e_cormerce.shoppe.repository.catgory.SynonymsRepository;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.service.category.helper.CategoryHelper;
import com.e_cormerce.shoppe.service.media.ImageService;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

  CategoryHelper categoryHelper;
  ImageService imageService;
  CategoryRepository categoryRepository;
  SynonymsRepository synonymsRepository;
  ProductRepository productRepository;
  ProductMapper productMapper;

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
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
      category.setParent(categoryHelper.findById(request.getParentId()));
    }

    categoryRepository.save(category);
    synonymsRepository.save(
        CategorySynonyms.builder().category(category).val(request.getName()).build());

    if (request.getSynonyms() != null) {
      request
          .getSynonyms()
          .forEach(
              relevant -> {
                synonymsRepository.save(
                    CategorySynonyms.builder().category(category).val(relevant).build());
              });
    }
  }

  public List<ProductCardResponse> getProductsByCategoryId(String category_id) {

    return productRepository.findProductsInCategory(category_id).stream()
        .map(
            product -> {
              ProductCardResponse response = productMapper.toProductDTO(product);
              return response;
            })
        .toList();
  }

  public CategoryDetailResponse getCategoryDetailResponse(String id) {
    return categoryRepository
        .findCategoryDetailsById(id)
        .orElseThrow(() -> new AppException(ErrorCode.EXISTED_CATEGORY));
  }

  public List<Category> getChildren(String id) {
    List<Category> children = categoryHelper.findChildren(id);
    return children;
  }

  public List<Category> getDefault() {
    List<Category> defaults = categoryHelper.findDefault();
    return defaults;
  }
}
