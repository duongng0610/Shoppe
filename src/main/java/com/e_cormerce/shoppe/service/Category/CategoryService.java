package com.e_cormerce.shoppe.service.Category;

import com.e_cormerce.shoppe.dto.request.Admin.CreateCategoryRequest;
import com.e_cormerce.shoppe.dto.request.Admin.GetChildrenCategoryRequest;
import com.e_cormerce.shoppe.dto.response.CreateCategoryResponse;
import com.e_cormerce.shoppe.dto.response.GetChildrenCategoryResponse;
import com.e_cormerce.shoppe.dto.response.GetDefaultCategoryResponse;
import com.e_cormerce.shoppe.entity.product.Category;
import com.e_cormerce.shoppe.repository.CategoryRepository;
import com.e_cormerce.shoppe.service.Category.helper.CreateCategoryHelper;
import com.e_cormerce.shoppe.service.media.CloudinaryService;
import com.e_cormerce.shoppe.service.media.ImageService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CategoryService {

    CreateCategoryHelper createCategoryHelper;
    CloudinaryService cloudinaryService;
    CategoryRepository categoryRepository;

    public CreateCategoryResponse create(CreateCategoryRequest request, MultipartFile thumbnail) {

        CompletableFuture<String> thumbnailFuture =
                cloudinaryService.uploadFile(thumbnail);

        CompletableFuture<Category> parentFuture =
                createCategoryHelper.findParent(request.getParent_id());

        CompletableFuture.allOf(thumbnailFuture, parentFuture).join();

        Category category = Category.builder()
                .val(request.getName())
                .parent(parentFuture.join())
                .thumbnail(thumbnailFuture.join())
                .build();

        categoryRepository.save(category);

        return CreateCategoryResponse.builder()
                .name(category.getVal())
                .thumbnail(category.getThumbnail())
                .parent_id(request.getParent_id())
                .build();
    }

    public List<Category> getDefaults() {
         return createCategoryHelper.findDefault();

    }

    public GetChildrenCategoryResponse getChildren(GetChildrenCategoryRequest request) {
        List<Category> children = createCategoryHelper.findChildren(request.getId());
        return GetChildrenCategoryResponse.builder()
                .children(children)
                .build();
    }

    public GetDefaultCategoryResponse getChildren() {
        List<Category> defaults = createCategoryHelper.findDefault();
        return GetDefaultCategoryResponse.builder()
                .defaults(defaults)
                .build();
    }


}