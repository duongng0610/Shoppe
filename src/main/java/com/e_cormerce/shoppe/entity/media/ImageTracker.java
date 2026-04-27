package com.e_cormerce.shoppe.entity.media;

import com.e_cormerce.shoppe.enums.media.ImageType;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Entity
@Table(indexes = {
        @Index(name = "idx_created_at", columnList = "created_at"),
        @Index(name = "idx_image_id", columnList = "image_id")
})
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ImageTracker {
  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  String id;

  @Column(name = "image_id", nullable = false, unique = true)
  String imageId;

  @Enumerated(EnumType.STRING)
  ImageType imageType;

  @Column(name = "created_at", nullable = false)
  LocalDateTime createdAt;
}
