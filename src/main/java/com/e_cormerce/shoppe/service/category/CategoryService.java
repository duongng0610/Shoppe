package com.e_cormerce.shoppe.service.category;

import com.e_cormerce.shoppe.dto.request.admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.response.category.CategoryDetailResponse;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.category.CategorySynonyms;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.event.catgory.CategorySearched;
import com.e_cormerce.shoppe.event.media.ImagesConfirmedEvent;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.product.CategoryMapper;
import com.e_cormerce.shoppe.mapper.product.ProductMapper;
import com.e_cormerce.shoppe.repository.catgory.CategoryRepository;
import com.e_cormerce.shoppe.repository.catgory.SynonymsRepository;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.service.category.helper.CategoryHelper;
import com.e_cormerce.shoppe.service.media.CloudinaryService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
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
  CloudinaryService cloudinaryService;

  @Transactional(isolation = Isolation.READ_UNCOMMITTED)
  public void create(CreateCategoryRequest request) {
    if (categoryRepository.existsByVal(request.getName())) {
      throw new AppException(ErrorCode.EXISTED_CATEGORY);
    }

    Set<String> usedImageId = new HashSet<>();

    Category category =
        Category.builder()
            .val(request.getName().toLowerCase())
            .thumbnail(cloudinaryService.generateResizedUrl(request.getThumbnailId(), 300, 300))
            .build();

    if (request.getThumbnailId() != null && !request.getThumbnailId().isBlank()) {
      usedImageId.add(request.getThumbnailId());
    }

    if (request.getParentId() != null) {
      Category parent = categoryHelper.findParent(request.getParentId());
      category.setPathToParent(
          parent.getPathToParent() == null
              ? parent.getId()
              : parent.getPathToParent() + "/" + parent.getId());
    }

    categoryRepository.save(category);

    if (usedImageId != null && !usedImageId.isEmpty()) {
      eventPublisher.publishEvent(ImagesConfirmedEvent.builder().usedImageIds(usedImageId).build());
    }

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
    Category category =
        categoryRepository
            .findById(category_id)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY));
    eventPublisher.publishEvent(
        CategorySearched.builder().category(categoryMapper.toDto(category)));
    return productRepository.findProductsInCategory(category_id).stream()
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

  public List<Category> getChildren(String id) {
    List<Category> children = categoryHelper.findChildren(id);
    return children;
  }

  public List<Category> getDefault() {
    List<Category> defaults = categoryHelper.findDefault();
    return defaults;
  }
}
