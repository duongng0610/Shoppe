package com.e_cormerce.shoppe.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.cloudinary.utils.ObjectUtils;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import com.e_cormerce.shoppe.properties.CloudinaryProperties;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {
    Cloudinary cloudinary;
    CloudinaryProperties cloudinaryProperties;

    /**
     * cấu hình tham số cho ảnh : width , height.
     *
     * @return
     */
    private Map getParams() {
        return ObjectUtils.asMap("transformation", new Transformation()
                .width(cloudinaryProperties.getImageWidth())
                .height(cloudinaryProperties.getImageHeight())
                /**
                 * crop: chế độ resize:
                 * -fill : cắt ảnh gốc
                 * -fit: thu nhỏ ảnh vừa với khung
                 * -scale: co ảnh
                 */
                .crop(cloudinaryProperties.getCrop())
        );
    }


    /**
     * upload file đơn.
     * trả về CompleteableFuture mà ko trả về thằng url vì để làm cơ chế song song , đưa vào thread pool trước rồi tất cả cùng chạy .
     *
     * @param file
     * @return
     */
    @Async
    public CompletableFuture<String> uploadFile(MultipartFile file) {
        try {
            Map<String, Object> data = this.cloudinary.uploader().upload(file.getBytes(), this.getParams());
            return CompletableFuture.completedFuture(data.get("secure_url").toString());
        } catch (IOException ioe) {
            throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
        }

    }


}
