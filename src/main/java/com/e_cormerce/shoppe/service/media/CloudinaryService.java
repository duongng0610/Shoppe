package com.e_cormerce.shoppe.service.media;

import com.cloudinary.Cloudinary;
import com.cloudinary.Transformation;
import com.e_cormerce.shoppe.entity.media.ImageTracker;
import com.e_cormerce.shoppe.enums.media.ImageType;
import com.e_cormerce.shoppe.repository.image.ImageTrackerRepository;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CloudinaryService {
  Cloudinary cloudinary;
  ImageTrackerRepository imageTrackerRepository;

  public Map<String, Object> generateUploadSignature(ImageType imageType) {
    long timestamp = System.currentTimeMillis() / 1000L;
    String generatedImageId = UUID.randomUUID().toString();
    String folderPath = String.format("shoppe/%s", imageType.getFolderName());

    Map<String, Object> paramsToSign = new HashMap<>();
    paramsToSign.put("timestamp", timestamp);
    paramsToSign.put("public_id", generatedImageId);
    paramsToSign.put("folder", folderPath);

    String apiSecret = cloudinary.config.apiSecret;
    String signature = cloudinary.apiSignRequest(paramsToSign, apiSecret);

    String fullPublicId = folderPath + "/" + generatedImageId;

    ImageTracker temp =
        ImageTracker.builder()
            .imageId(fullPublicId)
            .imageType(imageType)
            .createdAt(LocalDateTime.now())
            .build();

    imageTrackerRepository.save(temp);

    Map<String, Object> signatureData = new HashMap<>();
    signatureData.put("signature", signature);
    signatureData.put("timestamp", timestamp);
    signatureData.put("api_key", cloudinary.config.apiKey);
    signatureData.put("cloud_name", cloudinary.config.cloudName);
    signatureData.put("public_id", generatedImageId);
    signatureData.put("folder", folderPath);

    return signatureData;
  }

  public String generateResizedUrl(String publicId, int width, int height) {
    if (publicId == null || publicId.isEmpty()) return null;

    return cloudinary
        .url()
        .transformation(
            new Transformation()
                .width(width)
                .height(height)
                .crop("fit")
                .quality("auto")
                .fetchFormat("auto"))
        .generate(publicId);
  }
}
