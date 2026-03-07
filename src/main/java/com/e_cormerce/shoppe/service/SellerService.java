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
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class SellerService {
    CreateProductHelper createProductHelper;
    ProductRepository productRepository;
    UserRepository userRepository;
    ImageService imageService;
    ProductExtraImageRepository productExtraImageRepository;
    AuthService authService;
    CloudinaryService cloudinaryService;

    @Transactional(timeout = 15, isolation = Isolation.READ_UNCOMMITTED)
    public CreateProductResponse createProduct(CreateProductRequest request,
                                               MultipartFile thumbnail,
                                               boolean hasExtraImages,
                                               List<MultipartFile> extraImages,
                                               boolean hasVariant,
                                               List<MultipartFile> variantImages) {

        /**
         * Đẩy task upload thumbnail vào thread pool .
         */
        CompletableFuture<String> thumbnailFuture = this.cloudinaryService.uploadFile(thumbnail);

        /**
         * Đẩy task upload extra_images vào thread pool (đẩy upload từng item).
         */
        CompletableFuture<List<String>> extraImagesFuture = (extraImages != null && !extraImages.isEmpty()) ?
                this.imageService.uploadImageList(extraImages) :
                CompletableFuture.completedFuture(new ArrayList<>());

        /**
         * Đẩy task upload variantsImage vào thread pool (đẩy upload từng item).
         */
        CompletableFuture<List<String>> variantImagesFuture = (variantImages != null && !variantImages.isEmpty()) ?
                this.imageService.uploadImageList(variantImages) :
                CompletableFuture.completedFuture(new ArrayList<>());

        /**
         * Dùng allOf để chờ các task cùng join xong thì trả về 1 collect , có thể dùng hàm thenApply , thenLog ...
         */
        CompletableFuture.allOf(thumbnailFuture, extraImagesFuture, variantImagesFuture).join();

        /**
         * Lấy kết quả đã join.
         */
        String thumbnailUrl = thumbnailFuture.join();
        List<String> extraImagesUrl = extraImagesFuture.join();
        List<String> variantImagesUrl = variantImagesFuture.join();


        Product product = Product.builder()
                .name(request.getName())
                .description(request.getReason())
                .originPrice(request.getOriginPrice())
                .status(ProductStatus.PENDING)
                .created_at(LocalDateTime.now())
                .thumbnail(thumbnailUrl)
                .seller(authService.getUserThroughAuthentication())
                .build();


        if (extraImagesUrl != null && !extraImagesUrl.isEmpty()) {
            product.setProductExtraImages(extraImagesUrl.stream().map(url -> ProductExtraImage.builder().product(product).url(url).build()).toList());
        }
        if (request.getTypes() != null && !request.getTypes().isEmpty()) {
            product.setTypes(this.createTypes(request.getTypes(), product));
        }
        if (hasVariant) {
            if (variantImages.size() != request.getVariantRequests().size()) {//nếu số lượng ảnh ko giống nhau.
                throw new AppException(ErrorCode.INVALID_CREATE_VARIANTS);
            }
            product.setVariants(this.createVariants(request.getVariantRequests(), product, variantImagesUrl));
        } else {
            product.setVariants(this.createProductHelper.createDefaultVariant(product));
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

    /**
     * Cần hàm gọi hàm Async ở  1 bean khác thì mới tạo đyocwj async.
     *
     * @param variantRequests
     * @param product
     * @param variantImageUrls
     * @return
     */
    private List<Variant> createVariants(List<VariantRequest> variantRequests, Product product, List<String> variantImageUrls) {
        List<CompletableFuture<Variant>> variantFutures = new ArrayList<>();
        for (int i = 0; i < variantRequests.size(); i++) {
            variantFutures.add(this.createProductHelper.createVariantFuture(variantRequests.get(i), product, variantImageUrls.get(i)));
        }
        /**
         allOff : chờ tất cả cùng join xong thì lấy : success khi tất cả success , lỗi khi có ít nhất 1 thằng exception
         -Có thể dùng thenApply( logging) ...
         */
        CompletableFuture.allOf(variantFutures.toArray(new CompletableFuture[0])).join();
        return variantFutures.stream().map(CompletableFuture::join).toList();
    }

    /**
     * Cần hàm gọi hàm Async ở  1 bean khác thì mới tạo đyocwj async.
     *
     * @return
     */
    private List<Type> createTypes(List<TypeRequest> typeRequests, Product product) {
        List<CompletableFuture<Type>> typeFutures = typeRequests.stream().map(typeRequest -> this.createProductHelper.createType(typeRequest, product)).toList();
        /**
         allOff : chờ tất cả cùng join xong thì lấy : success khi tất cả success , lỗi khi có ít nhất 1 thằng exception
         -Có thể dùng thenApply( logging) ...
         */
        CompletableFuture.allOf(typeFutures.toArray(new CompletableFuture[0])).join();

        return typeFutures.stream().map(CompletableFuture::join).toList();
    }


}
