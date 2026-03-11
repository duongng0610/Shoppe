package com.e_cormerce.shoppe.service.category;

import com.e_cormerce.shoppe.dto.request.Admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.response.CreateCategoryResponse;
import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.CategoryRepository;
import com.e_cormerce.shoppe.service.category.helper.CreateCategoryHelper;
import com.e_cormerce.shoppe.service.media.CloudinaryService;
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
  CloudinaryService cloudinaryService;
  CategoryRepository categoryRepository;

  public CreateCategoryResponse create(CreateCategoryRequest request, MultipartFile thumbnail) {
    if (categoryRepository.existsByVal(request.getName())) {
      throw new AppException(ErrorCode.EXISTED_CATEGORY);
    }
    Category category =
        Category.builder()
            .val(request.getName())
            .thumbnail(cloudinaryService.uploadFileSync(thumbnail))
            .build();
    /** nếu khác null thì mơi theem parent , còn ko thì vẫn tạo với mức mặc định. */
    if (request.getParent_id() != null) {
      category.setParent(createCategoryHelper.findById(request.getParent_id()));
    }
    categoryRepository.save(category);

    return CreateCategoryResponse.builder()
        .name(category.getVal())
        .thumbnail(category.getThumbnail())
        .parent_id(request.getParent_id())
        .build();
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
