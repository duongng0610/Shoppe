package com.e_cormerce.shoppe.service;

import com.cloudinary.Cloudinary;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {
    Cloudinary cloudinary;


    /**
     * upload file đơn.
     *
     * @param file
     * @return
     */
    public String uploadImage(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return (String) data.get("secure_url");
        } catch (IOException ioe) {
            throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
        }
    }

    public List<String> uploadImageList(List<MultipartFile> files) {
            List<String> datas = new ArrayList<>();
            for (MultipartFile file : files) {
                var data = this.uploadImage(file);
                datas.add(data);
            }
            return datas;
    }
}
