package com.e_cormerce.shoppe.service;

import com.cloudinary.Cloudinary;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.e_cormerce.shoppe.exception.AppException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {
    private final Cloudinary cloudinary;


    /**
     * upload file đơn.
     *
     * @param file
     * @return
     */
    public Map uploadImage(MultipartFile file) {
        try {
            Map data = this.cloudinary.uploader().upload(file.getBytes(), Map.of());
            return data;
        } catch (IOException ioe) {
            throw new AppException(ErrorCode.INVALID_FILE_FORMAT);
        }
    }
}
