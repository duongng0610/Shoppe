package com.e_cormerce.shoppe.service.product;

import com.e_cormerce.shoppe.dto.common.catgory.CategoryDto;
import com.e_cormerce.shoppe.dto.common.user.UserDto;
import com.e_cormerce.shoppe.dto.request.product.CreateProductRequest;
import com.e_cormerce.shoppe.dto.request.product.CreateProductReviewRequest;
import com.e_cormerce.shoppe.dto.response.product.MyProductResponse;
import com.e_cormerce.shoppe.dto.response.product.ProductCardResponse;
import com.e_cormerce.shoppe.dto.response.product.ProductDetailResponse;
import com.e_cormerce.shoppe.dto.response.product.VariantDetailResponse;
import com.e_cormerce.shoppe.entity.category.Category;
import com.e_cormerce.shoppe.entity.order.Order;
import com.e_cormerce.shoppe.entity.product.*;
import com.e_cormerce.shoppe.entity.user.User;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.product.ProductStatus;
import com.e_cormerce.shoppe.event.product.ProductCreated;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.mapper.product.CategoryMapper;
import com.e_cormerce.shoppe.mapper.product.ProductMapper;
import com.e_cormerce.shoppe.mapper.user.UserMapper;
import com.e_cormerce.shoppe.projection.overview.OverviewProductSystem;
import com.e_cormerce.shoppe.projection.product.ProductCardProjection;
import com.e_cormerce.shoppe.projection.product.ProductFullViewProjection;
import com.e_cormerce.shoppe.repository.catgory.CategoryRepository;
import com.e_cormerce.shoppe.repository.order.OrderRepository;
import com.e_cormerce.shoppe.repository.product.ProductRepository;
import com.e_cormerce.shoppe.repository.product.ProductReviewRepository;
import com.e_cormerce.shoppe.repository.product.VariantRepository;
import com.e_cormerce.shoppe.service.auth.AuthService;
import com.e_cormerce.shoppe.service.product.helper.CreateProductHelper;
import com.e_cormerce.shoppe.service.product.helper.CreateProductReviewHelper;
import com.e_cormerce.shoppe.service.product.helper.GetProductDetailsHelper;
import com.e_cormerce.shoppe.service.product.helper.ProductQueryDBHelper;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

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
    OrderRepository orderRepository;
    CreateProductReviewHelper createProductReviewHelper;
    ProductReviewRepository productReviewRepository;


    @Transactional(isolation = Isolation.READ_UNCOMMITTED, timeout = 10)
    public Product persistProduct(@Valid CreateProductRequest request) {
        var user = authService.getUserThroughAuthentication();
        Product product =
                Product.builder()
                        .name(request.getName())
                        .description(request.getDescription())
                        .status(ProductStatus.PENDING)
                        .createdAt(LocalDateTime.now())
                        .hasVariant(request.getHasVariant())
                        .thumbnail(request.getThumbnailUrl())
                        .originPrice(request.getOriginPrice())
                        .seller(user)
                        .build();

        if (request.getExtraImageUrls() != null && !request.getExtraImageUrls().isEmpty()) {
            product.setProductExtraImages(
                    request.getExtraImageUrls().stream()
                            .map(url -> ProductExtraImage.builder().product(product).url(url).build())
                            .collect(Collectors.toSet()));
        }

        if (request.getTypes() != null && !request.getTypes().isEmpty()) {
            product.setTypes(createProductHelper.createType(request.getTypes(), product));
        }
        if (request.getHasVariant()) {
            if (request.getVariantRequests() != null && !request.getVariantRequests().isEmpty()) {

                for (int i = 0; i < request.getVariantRequests().size(); i++) {
                    product.addVariant(
                            createProductHelper.createVariant(request.getVariantRequests().get(i), product));
                }
            }
        } else {
            product.addVariant(createProductHelper.createDefaultVariant(product, request));
        }

        product.setCategory(
                categoryRepository
                        .findById(request.getCategoryId())
                        .orElseThrow(() -> new AppException(ErrorCode.NOT_EXISTED_CATEGORY)));

        productRepository.save(product);

        eventPublisher.publishEvent(
                ProductCreated.builder()
                        .product(productMapper.toProductCardDto(product))
                        .seller(userMapper.toDto(user))
                        .category(categoryMapper.toDto(product.getCategory()))
                        .build());

        return product;
    }

    public List<ProductCardResponse> getProductForHome(int limit, int offset) {
        List<Product> products = productRepository.findProductForHome(limit, offset);
        return products.stream().map(productMapper::toProductCardDto).toList();
    }

    //b1: conver mapper product=>product
    @Transactional(readOnly = true)
    public MyProductResponse cleanGetProductDetails(String id) {
        var product = productRepository.findFetchProductById(id).orElseThrow(() -> new AppException(ErrorCode.NOT_EXIST_PRODUCT));
        return productMapper.toMyProductDTO(product);
    }

    @Transactional(timeout = 5)
    @Cacheable(value = "products", key = "#id")
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

    // =====================================
    // PRODUCT FULL VIEW
    // =====================================

    public List<ProductFullViewProjection> getProductFullViews(Integer limit, Integer offset) {

        return productRepository.getProductFullViews(limit, offset);
    }

    @Transactional
    public void createReview(@Valid CreateProductReviewRequest request) {
        Order order =
                orderRepository
                        .findById(request.getOrderId())
                        .orElseThrow(() -> new AppException(ErrorCode.UNABLE_REVIEW_PRODUCT));

        User client = authService.getUserThroughAuthentication();

        Product product = createProductReviewHelper.getProduct(order.getId());

        ProductReview productReview =
                ProductReview.builder()
                        .rate(request.getRate())
                        .description(request.getDescription())
                        .client(client)
                        .product(product)
                        .build();

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            createProductReviewHelper.createProductReviewImage(request, productReview);
        }

        productReviewRepository.save(productReview);
    }

    public List<ProductCardProjection> getProductsInHomePage(int limit, int offset) {

        return productRepository.getProductsInHomePage(limit, offset);
    }

    public List<ProductCardProjection> getProductsInSellerPage(
            String sellerId, int limit, int offset) {
        return productRepository.getProductsInSellerPage(sellerId, limit, offset);
    }

    public OverviewProductSystem getProductSystems() {
        return productRepository.getProductOverview();
    }
}
