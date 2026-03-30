package com.e_cormerce.shoppe.service.media;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.properties.CloudinaryProperties;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {
  Cloudinary cloudinary;

  private byte[] getOptizedImage (MultipartFile file) {
      try {
      ByteArrayOutputStream os = new ByteArrayOutputStream();
          Thumbnails.of(file.getInputStream())
                  .size(800, 800)          // resize trước
                  .outputQuality(0.7)      // nén trước
                  .toOutputStream(os);

         return os.toByteArray();
      }catch (Exception e) {
          throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
      }
  }



  /**
   * upload file đơn. trả về CompleteableFuture mà ko trả về thằng url vì để làm cơ chế song song ,
   * đưa vào thread pool trước rồi tất cả cùng chạy .
   *
   * @param file
   * @return
   */
  @Async("uploadExecutor")
  public CompletableFuture<String> uploadFile(MultipartFile file) {
    try {
      Map<String, Object> data =
          this.cloudinary.uploader().upload(getOptizedImage(file), ObjectUtils.emptyMap());
      return CompletableFuture.completedFuture(data.get("secure_url").toString());
    } catch (IOException ioe) {
      // neu de app exception se bi wrap lai do dang chay trong luong rieng theo co che async
      return CompletableFuture.failedFuture(new AppException(ErrorCode.INVALID_FILE_FORMAT));
    }
  }

  public String uploadFileSync(MultipartFile file) {
    try {
      Map<String, Object> data =
          this.cloudinary.uploader().upload(getOptizedImage(file), ObjectUtils.emptyMap());
      return data.get("secure_url").toString();
    } catch (IOException ioe) {
      // neu de app exception se bi wrap lai do dang chay trong luong rieng theo co che async
      throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
    }
  }
}
