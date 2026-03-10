package com.e_cormerce.shoppe.service.Category;

import com.e_cormerce.shoppe.dto.request.Admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.request.Admin.GetChildrenCategoryRequest;
import com.e_cormerce.shoppe.dto.response.CreateCategoryResponse;
import com.e_cormerce.shoppe.dto.response.GetChildrenCategoryResponse;
import com.e_cormerce.shoppe.dto.response.GetDefaultCategoryResponse;
import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.CategoryRepository;
import com.e_cormerce.shoppe.service.Category.helper.CreateCategoryHelper;
import com.e_cormerce.shoppe.service.media.CloudinaryService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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
        Category category = Category.builder()
                .val(request.getName())
                .thumbnail(cloudinaryService.uploadFileSync(thumbnail))
                .build();
        /**
         * nếu khác null thì mơi theem parent , còn ko thì vẫn tạo với mức mặc định  .
         */
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


    public GetChildrenCategoryResponse getDirectChildren(GetChildrenCategoryRequest request) {
        List<Category> children = createCategoryHelper.findChildren(request.getId());
        return GetChildrenCategoryResponse.builder()
                .children(children)
                .build();
    }

    public GetDefaultCategoryResponse getDirectChildren() {
        List<Category> defaults = createCategoryHelper.findDefault();
        return GetDefaultCategoryResponse.builder()
                .defaults(defaults)
                .build();
    }


}