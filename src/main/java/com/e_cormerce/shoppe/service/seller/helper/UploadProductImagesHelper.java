package com.e_cormerce.shoppe.service.seller.helper;

import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.service.media.CloudinaryService;
import com.e_cormerce.shoppe.service.media.ImageService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UploadProductImagesHelper {
  CloudinaryService cloudinaryService;
  ImageService imageService;

  /**
   * Đa luồng ở mỗi ảnh được upload.
   *
   * @param thumbnail
   * @param extraImages
   * @param variantImages
   * @return
   */
  public ProductImagesUrl uploadImagesOfProduct(
      MultipartFile thumbnail, List<MultipartFile> extraImages, List<MultipartFile> variantImages) {

    /** Đẩy task upload thumbnail vào thread pool . */
    CompletableFuture<String> thumbnailFuture =
        cloudinaryService
            .uploadFile(thumbnail)
            .exceptionally(
                (e) -> {
                  throw new AppException(ErrorCode.UPLOAD_FAILED);
                });

    /** Đẩy task upload extra_images vào thread pool (đẩy upload từng item). */
    CompletableFuture<List<String>> extraImagesFuture =
        (extraImages != null && !extraImages.isEmpty())
            ? imageService
                .uploadImageList(extraImages)
                .exceptionally(
                    e -> {
                      throw new AppException(ErrorCode.UPLOAD_FAILED);
                    })
            : CompletableFuture.completedFuture(List.of());

    /** Đẩy task upload variantsImage vào thread pool (đẩy upload từng item). */
    CompletableFuture<List<String>> variantImagesFuture =
        (variantImages != null && !variantImages.isEmpty())
            ? imageService
                .uploadImageList(variantImages)
                .exceptionally(
                    e -> {
                      throw new AppException(ErrorCode.UPLOAD_FAILED);
                    })
            : CompletableFuture.completedFuture(List.of());

    try {
      /** limit time of join (avoid deadlock when cloudinary is lag) */
      CompletableFuture.allOf(thumbnailFuture, extraImagesFuture, variantImagesFuture)
          .get(30, TimeUnit.SECONDS);

    } catch (TimeoutException e) {
      throw new AppException(ErrorCode.UPLOAD_TIMEOUT);

    } catch (ExecutionException e) {
      throw new AppException(ErrorCode.UPLOAD_FAILED);

    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new AppException(ErrorCode.UPLOAD_FAILED);
    }

    return new ProductImagesUrl(
        thumbnailFuture.join(), extraImagesFuture.join(), variantImagesFuture.join());
  }
}
