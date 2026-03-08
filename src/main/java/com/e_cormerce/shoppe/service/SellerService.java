package com.e_cormerce.shoppe.service;

import com.e_cormerce.shoppe.dto.request.CreateProductRequest;
import com.e_cormerce.shoppe.dto.request.TypeRequest;
import com.e_cormerce.shoppe.dto.request.VariantRequest;
import com.e_cormerce.shoppe.dto.response.CreateProductResponse;
import com.e_cormerce.shoppe.entity.product.Product;
import com.e_cormerce.shoppe.entity.product.ProductExtraImage;
import com.e_cormerce.shoppe.entity.product.Type;
import com.e_cormerce.shoppe.entity.product.Variant;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.enums.ProductStatus;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.repository.ProductExtraImageRepository;
import com.e_cormerce.shoppe.repository.ProductRepository;
import com.e_cormerce.shoppe.repository.UserRepository;
import com.e_cormerce.shoppe.service.seller.helper.CreateProductHelper;
import com.e_cormerce.shoppe.service.seller.helper.UploadUrls;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerService {
    CreateProductHelper createProductHelper;
    ProductRepository productRepository;
    ImageService imageService;
    AuthService authService;
    CloudinaryService cloudinaryService;

    @Qualifier("uploadExecutor")
    Executor imageUploadExecutor;

    public CreateProductResponse createProduct(CreateProductRequest request,
                                               MultipartFile thumbnail,
                                               boolean hasExtraImages,
                                               List<MultipartFile> extraImages,
                                               boolean hasVariant,
                                               List<MultipartFile> variantImages) {


        if (hasVariant && variantImages != null &&
                variantImages.size() != request.getVariantRequests().size()) {
            throw new AppException(ErrorCode.INVALID_CREATE_VARIANTS);
        }

        UploadUrls urls = uploadAllImages(thumbnail, extraImages, variantImages);

        return persistProduct(request, urls, hasVariant);
    }



    private UploadUrls uploadAllImages(
            MultipartFile thumbnail,
            List<MultipartFile> extraImages,
            List<MultipartFile> variantImages) {

        /**
         * Đẩy task upload thumbnail vào thread pool .
         */
        CompletableFuture<String> thumbnailFuture = cloudinaryService.uploadFile(thumbnail)
                .exceptionally((e) -> { throw new AppException(ErrorCode.UPLOAD_FAILED); });

        /**
         * Đẩy task upload extra_images vào thread pool (đẩy upload từng item).
         */
        CompletableFuture<List<String>> extraImagesFuture =
                (extraImages != null && !extraImages.isEmpty())
                        ? imageService.uploadImageList(extraImages)
                        .exceptionally(e -> { throw new AppException(ErrorCode.UPLOAD_FAILED); })
                        : CompletableFuture.completedFuture(List.of());


        /**
         * Đẩy task upload variantsImage vào thread pool (đẩy upload từng item).
         */
        CompletableFuture<List<String>> variantImagesFuture =
                (variantImages != null && !variantImages.isEmpty())
                        ? imageService.uploadImageList(variantImages)
                        .exceptionally(e -> { throw new AppException(ErrorCode.UPLOAD_FAILED); })
                        : CompletableFuture.completedFuture(List.of());

        try {
            /**
             * limit time of join (avoid deadlock when cloudinary is lag)
             *
             */
            CompletableFuture.allOf(thumbnailFuture, extraImagesFuture, variantImagesFuture)
                    .get(30, TimeUnit.SECONDS);

        } catch(TimeoutException e) {
            throw new AppException(ErrorCode.UPLOAD_TIMEOUT);

        } catch (ExecutionException e) {
            throw new AppException(ErrorCode.UPLOAD_FAILED);

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new AppException(ErrorCode.UPLOAD_FAILED);
        }

        return new UploadUrls(
                thumbnailFuture.join(),
                extraImagesFuture.join(),
                variantImagesFuture.join()
        );
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, timeout = 10)
    protected CreateProductResponse persistProduct(
            CreateProductRequest request,
            UploadUrls urls,
            boolean hasVariant) {

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getReason())
                .originPrice(request.getOriginPrice())
                .status(ProductStatus.PENDING)
                .created_at(LocalDateTime.now())
                .thumbnail(urls.getThumbnailUrl())
                .seller(authService.getUserThroughAuthentication())
                .build();

        if (!urls.getExtraImageUrls().isEmpty()) {
            product.setProductExtraImages(
                    urls.getExtraImageUrls().stream()
                            .map(url -> ProductExtraImage.builder().product(product).url(url).build())
                            .toList()
            );
        }

        if (request.getTypes() != null && !request.getTypes().isEmpty()) {
            product.setTypes(createProductHelper.createType(request.getTypes(), product));
        }

        if (hasVariant) {
            product.setVariants(createProductHelper.createVariants(request.getVariantRequests(),
                    product, urls.getVariantImageUrls()));
        } else {
            product.setVariants(createProductHelper.createDefaultVariant(product));
        }

        productRepository.save(product);

        return CreateProductResponse.builder()
                .name(request.getName())
                .reason(request.getReason())
                .originPrice(request.getOriginPrice())
                .created_at(LocalDateTime.now())
                .total_quantity(request.getTotalQuantity())
                .types(product.getTypes())
                .variants(product.getVariants())
                .build();
    }


}
