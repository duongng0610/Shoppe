package com.e_cormerce.shoppe.service.product;

import com.e_cormerce.shoppe.dto.common.*;
import com.e_cormerce.shoppe.dto.request.seller.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.CreateProductResponse;
import com.e_cormerce.shoppe.dto.response.GetProductDetailResponse;
import com.e_cormerce.shoppe.entity.product.*;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.ProductStatus;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.product.CategoryMapper;
import com.e_cormerce.shoppe.mapper.product.ProductMapper;
import com.e_cormerce.shoppe.mapper.product.SellerMapper;
import com.e_cormerce.shoppe.repository.CategoryRepository;
import com.e_cormerce.shoppe.repository.ProductRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.product.helper.CreateProductHelper;
import com.e_cormerce.shoppe.service.product.helper.GetProductDetailsHelper;
import com.e_cormerce.shoppe.service.product.helper.ProductQueryDBHelper;
import com.e_cormerce.shoppe.service.seller.helper.ProductImagesUrl;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductService {
    AuthService authService;
    CreateProductHelper createProductHelper;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    ProductQueryDBHelper productQueryDBHelper;
    SellerMapper sellerMapper;
    CategoryMapper categoryMapper;
    GetProductDetailsHelper getProductDetailsHelper;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, timeout = 10)
    public CreateProductResponse persistProduct(
            CreateProductRequest request, ProductImagesUrl urls) {

        Product product =
                Product.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .originPrice(request.getOriginPrice())
                        .status(ProductStatus.PENDING)
                        .created_at(LocalDateTime.now())
                        .thumbnail(urls.getThumbnailUrl())
                        .seller(authService.getUserThroughAuthentication())
                        .originPrice(request.getOriginPrice())
                        .build();

        if (!urls.getExtraImageUrls().isEmpty()) {
            product.setProductExtraImages(
                    urls.getExtraImageUrls().stream()
                            .map(url -> ProductExtraImage.builder().product(product).url(url).build())
                            .toList());
        }

        if (request.getTypes() != null && !request.getTypes().isEmpty()) {
            product.setTypes(createProductHelper.createType(request.getTypes(), product));
        }

        if (urls.getVariantImageUrls() != null && !urls.getVariantImageUrls().isEmpty()) {
            product.setVariants(
                    createProductHelper.createVariants(
                            request.getVariantRequests(), product, urls.getVariantImageUrls()));
        } else {
            product.setVariants(createProductHelper.createDefaultVariant(product));
        }

        product.setCategory(
                categoryRepository
                        .findById(request.getCategoryId())
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY)));

        productRepository.save(product);

        return CreateProductResponse.builder()
                .name(request.getName())
                .description(request.getDescription())
                .originPrice(request.getOriginPrice())
                .created_at(LocalDateTime.now())
                .total_quantity(request.getTotalQuantity())
                .types(product.getTypes())
                .variants(product.getVariants())
                .build();
    }

    public List<ProductDTO> getProductForHome(int limit, int offset) {
        List<Product> products = productRepository.findProductForHome(limit, offset);
        return products.stream().map(productMapper::toProductDTO).toList();
    }

    public GetProductDetailResponse getProductDetail(String id) {
        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_PRODUCT));

        CompletableFuture<List<Type>> typesFuture = productQueryDBHelper.getTypes(id);
        CompletableFuture<List<Variant>> variantsFuture = productQueryDBHelper.getVariants(id);
        CompletableFuture<User> shopFuture = productQueryDBHelper.getSeller(id);
        CompletableFuture<Category> categoryFuture = productQueryDBHelper.getCategory(id);

        CompletableFuture.allOf(typesFuture, variantsFuture, shopFuture, categoryFuture).join();

        List<Type> types = typesFuture.join();
        List<Variant> variants = variantsFuture.join();
        User seller = shopFuture.join();
        Category category = categoryFuture.join();

        SellerDTO sellerResponse = sellerMapper.toSellerDTO(seller);
        CategoryDTO categoryResponse = categoryMapper.toCategoryDTO(category);

        List<VariantDetailResponse> variantResponses =
                getProductDetailsHelper.createVariantDetail(variants);
        List<TypeResponse> typeResponses = getProductDetailsHelper.createTypesResponse(types);

        return GetProductDetailResponse.builder()
                .id(product.getId())
                .category(categoryResponse)
                .seller(sellerResponse)
                .variants(variantResponses)
                .types(typeResponses)
                .build();
    }
}
