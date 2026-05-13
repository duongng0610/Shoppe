package com.e_cormerce.shoppe.service.category;

import com.e_cormerce.shoppe.dto.common.catgory.CategoryDto;
import com.e_cormerce.shoppe.dto.request.admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.request.admin.UpdateCategoryRequest;
import com.e_cormerce.shoppe.dto.response.category.CategoryDetailResponse;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.category.CategorySynonyms;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.event.catgory.CategorySearched;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.product.CategoryMapper;
import com.e_cormerce.shoppe.mapper.product.ProductMapper;
import com.e_cormerce.shoppe.projection.category.CategoryProjection;
import com.e_cormerce.shoppe.repository.catgory.CategoryRepository;
import com.e_cormerce.shoppe.repository.catgory.SynonymsRepository;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.service.category.helper.CategoryHelper;
import java.util.List;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

  CategoryHelper categoryHelper;
  CategoryRepository categoryRepository;
  SynonymsRepository synonymsRepository;
  ProductRepository productRepository;
  ProductMapper productMapper;
  ApplicationEventPublisher eventPublisher;
  CategoryMapper categoryMapper;

  @Transactional
  public void create(CreateCategoryRequest request) {
    if (categoryRepository.existsByVal(request.getName())) {
      throw new AppException(ErrorCode.EXISTED_CATEGORY);
    }
    Category category =
        Category.builder()
            .val(request.getName().toLowerCase())
            .thumbnail(request.getThumbnailUrl())
            .build();

    if (request.getParentId() != null) {
      Category parent = categoryHelper.findParent(request.getParentId());
      category.setPathToParent(
          parent.getPathToParent() == null
              ? "/" + parent.getId() + "/"
              : parent.getPathToParent() + parent.getId() + "/");
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

  // Lấy danh sách sản phẩm của danh mục với phân trang
  // Ví dụ: Lấy danh sách sản phẩm, giới hạn 20 bản ghi, bỏ qua 0 bản ghi đầu
  public List<ProductCardResponse> getProductsByCategoryId(String category_id) {
    return getProductsByCategoryId(category_id, 20, 0);
  }

  // Lấy danh sách sản phẩm của danh mục với phân trang tùy chỉnh
  public List<ProductCardResponse> getProductsByCategoryId(
      String category_id, int limit, int offset) {
    Category category =
        categoryRepository
            .findById(category_id)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
    eventPublisher.publishEvent(
        CategorySearched.builder().category(categoryMapper.toDto(category)));
    return productRepository.findProductsInCategory(category_id, limit, offset).stream()
        .map(
            product -> {
              ProductCardResponse response = productMapper.toProductCardDto(product);
              return response;
            })
        .toList();
  }

  public CategoryDetailResponse getCategoryDetailResponse(String id) {
    return categoryRepository
        .findCategoryDetailsById(id)
        .orElseThrow(() -> new AppException(ErrorCode.EXISTED_CATEGORY));
  }

  public List<CategoryProjection> getChildren(String id) {
    var children = categoryRepository.findChildren(id);
    return children;
  }

  public List<Category> getDefault() {
    List<Category> defaults = categoryHelper.findDefault();
    return defaults;
  }

  public CategoryDto deleteCategory(String id) {
    Category category =
        categoryRepository
            .findById(id)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
    if (categoryRepository.countProducts(category.getId()) > 0) {
      throw new AppException(ErrorCode.UNABLE_DELETE_CATEGORY);
    }
    categoryRepository.deleteById(category.getId());
    return categoryMapper.toDto(category);
  }

  public CategoryDto update(String id, UpdateCategoryRequest request) {
    Category category =
        categoryRepository
            .findAllById(id)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
    if (request.getVal() != null) {
      if (categoryRepository.findByVal(request.getVal()) == null) {
        category.setVal(request.getVal());
      }
      if (categoryRepository.countDeletedCategoryByVal(request.getVal()) > 0) {
        category.setDeleted(false);
      }
    }
    if (request.getThumbnail() != null) {
      category.setThumbnail(request.getThumbnail());
    }
    categoryRepository.save(category);
    return categoryMapper.toDto(category);
  }
}
