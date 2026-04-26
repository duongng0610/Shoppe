package com.e_cormerce.shoppe.controller.media;

import com.e_cormerce.shoppe.enums.media.ImageType;
import com.e_cormerce.shoppe.service.media.CloudinaryService;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/image")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ImageController {
  CloudinaryService cloudinaryService;

  @GetMapping("/generate-signature")
  public ResponseEntity<Map<String, Object>> getSignature(@RequestParam("type") ImageType type) {
    return ResponseEntity.ok(cloudinaryService.generateUploadSignature(type));
  }
}
