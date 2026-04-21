package com.e_cormerce.shoppe.service.product;

import com.e_cormerce.shoppe.dto.common.catgory.CategoryDto;
import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import com.e_cormerce.shoppe.dto.response.product.ProductDetailResponse;
import com.e_cormerce.shoppe.dto.response.product.VariantDetailResponse;
import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.product.ProductExtraImage;
import com.e_cormerce.shoppe.entity.product.Type;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.product.ProductStatus;
import com.e_cormerce.shoppe.event.product.ProductCreated;
import com.e_cormerce.shoppe.event.product.ProductViewed;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.product.CategoryMapper;
import com.e_cormerce.shoppe.mapper.product.ProductMapper;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.repository.catgory.CategoryRepository;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.product.helper.CreateProductHelper;
import com.e_cormerce.shoppe.service.product.helper.GetProductDetailsHelper;
import com.e_cormerce.shoppe.service.product.helper.ProductQueryDBHelper;
import com.e_cormerce.shoppe.service.seller.helper.ProductImagesUrl;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Validated
public class ProductService {
    AuthService authService;
    CreateProductHelper createProductHelper;
    ProductRepository productRepository;
    CategoryRepository categoryRepository;
    ProductMapper productMapper;
    ProductQueryDBHelper productQueryDBHelper;
    UserMapper userMapper;
    CategoryMapper categoryMapper;
    GetProductDetailsHelper getProductDetailsHelper;
    ApplicationEventPublisher eventPublisher;
    VariantRepository variantRepository;

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, timeout = 10)
    public Product persistProduct(@Valid CreateProductRequest request, ProductImagesUrl urls) {
        var user = authService.getUserThroughAuthentication();
        Product product =
                Product.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .originPrice(request.getOriginPrice())
                        .status(ProductStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .hasVariant(request.getHasVariant())
                        .thumbnail(urls.getThumbnailUrl())
                        .discountPercentage(request.getDiscountPercentage())
                        .seller(user)
                        .totalQuantity(request.getTotalQuantity())
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
        if (request.getHasVariant()) {
            if (urls.getVariantImageUrls() != null && !urls.getVariantImageUrls().isEmpty()) {

                for (int i = 0; i < request.getVariantRequests().size(); i++) {
                    product.addVariant(createProductHelper.createVariant(request.getVariantRequests().get(i), product, urls.getVariantImageUrls().get(i)));
                }

            }
        } else {
            product.addVariant(createProductHelper.createDefaultVariant(product));
        }

        product.setCategory(
                categoryRepository
                        .findById(request.getCategoryId())
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY)));

        productRepository.save(product);

        eventPublisher.publishEvent(ProductCreated.builder().product(productMapper.toProductCardDto(product)).seller(userMapper.toDto(user)).category(categoryMapper.toDto(product.getCategory())).build());

        return product;
    }

    public List<ProductCardResponse> getProductForHome(int limit, int offset) {
        List<Product> products = productRepository.findProductForHome(limit, offset);
        return products.stream().map(productMapper::toProductCardDto).toList();
    }

    public ProductDetailResponse getProductDetail(String id) {
        Product product =
                productRepository
                        .findById(id)
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_PRODUCT));

        CompletableFuture<List<ProductExtraImage>> imagesFuture =
                productQueryDBHelper.getExtraImagesOfProduct(id);
        CompletableFuture<List<Type>> typesFuture = productQueryDBHelper.getTypes(id);
        CompletableFuture<List<Variant>> variantsFuture = productQueryDBHelper.getVariants(id);
        CompletableFuture<User> shopFuture = productQueryDBHelper.getSeller(id);
        CompletableFuture<Category> categoryFuture = productQueryDBHelper.getCategory(id);

        CompletableFuture.allOf(typesFuture, variantsFuture, shopFuture, categoryFuture, imagesFuture)
                .join();

        List<Type> types = typesFuture.join();
        List<Variant> variants = variantsFuture.join();
        List<ProductExtraImage> images = imagesFuture.join();
        User seller = shopFuture.join();
        Category category = categoryFuture.join();

        UserDto sellerResponse = userMapper.toDto(seller);
        CategoryDto categoryResponse = categoryMapper.toDto(category);

        List<VariantDetailResponse> variantResponses =
                getProductDetailsHelper.createVariantDetail(variants);
        var typeResponses = getProductDetailsHelper.createTypesResponse(types);

        eventPublisher.publishEvent(ProductViewed.builder().viewedAt(LocalDateTime.now()).productId(id).build());

        return ProductDetailResponse.builder()
                .hasVariant(product.isHasVariant())
                .name(product.getName())
                .id(product.getId())
                .category(categoryResponse)
                .seller(sellerResponse)
                .variants(variantResponses)
                .types(typeResponses)
                .thumbnail(product.getThumbnail())
                .originPrice(product.getOriginPrice())
                .totalSoldQuantity(product.getTotalQuantity())
                .totalQuantity(product.getTotalQuantity())
                .extraImages(images.stream().map(item -> item.getUrl()).toList())
                .build();
    }

    public List<ProductCardResponse> getProductOfSeller(String id, int limit, int offset) {
        List<Product> products = productRepository.findProductsOfSeller(id, limit, offset);
        return products.stream().map(productMapper::toProductCardDto).toList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void updateQuantity(String variantId, String productId, int quantityOrdered) {
        variantRepository.updateQuantitySold(variantId, quantityOrdered);
        productRepository.updateQuantitySold(productId, quantityOrdered);
    }
}
