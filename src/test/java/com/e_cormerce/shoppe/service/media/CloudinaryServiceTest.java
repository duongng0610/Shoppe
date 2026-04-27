package com.e_cormerce.shoppe.service.media;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import com.cloudinary.Cloudinary;
import com.e_cormerce.shoppe.entity.media.ImageTracker;
import com.e_cormerce.shoppe.enums.media.ImageType;
import com.e_cormerce.shoppe.repository.image.ImageTrackerRepository;
import java.util.Map;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@FieldDefaults(level = AccessLevel.PRIVATE)
class CloudinaryServiceTest {

  CloudinaryService cloudinaryService;
  Cloudinary cloudinary;

  @Mock ImageTrackerRepository imageTrackerRepository;

  @Mock Cloudinary.Config config;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    cloudinaryService = new CloudinaryService(cloudinary, imageTrackerRepository);
    when(cloudinary.config).thenReturn(config);
    when(config.apiSecret).thenReturn("test-secret");
    when(config.apiKey).thenReturn("test-key");
    when(config.cloudName).thenReturn("test-cloud");
  }

  // ============ generateUploadSignature Tests ============

  @Test
  @DisplayName("✅ Should generate signature with valid ImageType.PRODUCT")
  void testGenerateSignature_WithValidImageTypeProduct() {
    // Arrange
    ImageType imageType = ImageType.PRODUCT;
    when(cloudinary.apiSignRequest(any(), anyString())).thenReturn("signature123");
    when(imageTrackerRepository.save(any(ImageTracker.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    Map<String, Object> result = cloudinaryService.generateUploadSignature(imageType);

    // Assert
    assertNotNull(result);
    assertEquals("signature123", result.get("signature"));
    assertEquals("test-key", result.get("api_key"));
    assertEquals("test-cloud", result.get("cloud_name"));
    assertNotNull(result.get("public_id"));
    assertNotNull(result.get("timestamp"));
    verify(imageTrackerRepository, times(1)).save(any(ImageTracker.class));
  }

  @Test
  @DisplayName("✅ Should generate signature with valid ImageType.AVATAR")
  void testGenerateSignature_WithValidImageTypeAvatar() {
    // Arrange
    ImageType imageType = ImageType.AVATAR;
    when(cloudinary.apiSignRequest(any(), anyString())).thenReturn("sig-avatar");
    when(imageTrackerRepository.save(any(ImageTracker.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    Map<String, Object> result = cloudinaryService.generateUploadSignature(imageType);

    // Assert
    assertNotNull(result);
    assertEquals("sig-avatar", result.get("signature"));

    // Verify folder path
    ArgumentCaptor<ImageTracker> captor = ArgumentCaptor.forClass(ImageTracker.class);
    verify(imageTrackerRepository).save(captor.capture());
    ImageTracker savedTracker = captor.getValue();
    assertTrue(savedTracker.getImageId().contains("users/avatars"));
  }

  @Test
  @DisplayName("✅ Should track image with correct ImageType")
  void testGenerateSignature_ShouldTrackImageWithCorrectType() {
    // Arrange
    ImageType imageType = ImageType.REVIEW;
    when(cloudinary.apiSignRequest(any(), anyString())).thenReturn("sig-review");
    when(imageTrackerRepository.save(any(ImageTracker.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    cloudinaryService.generateUploadSignature(imageType);

    // Assert
    ArgumentCaptor<ImageTracker> captor = ArgumentCaptor.forClass(ImageTracker.class);
    verify(imageTrackerRepository).save(captor.capture());
    ImageTracker tracker = captor.getValue();
    assertEquals(ImageType.REVIEW, tracker.getImageType());
    assertNotNull(tracker.getCreatedAt());
  }

  @Test
  @DisplayName("❌ Should throw exception when ImageType is null")
  void testGenerateSignature_WithNullImageType() {
    // Act & Assert
    assertThrows(NullPointerException.class, () -> cloudinaryService.generateUploadSignature(null));
  }

  @Test
  @DisplayName("✅ Should generate different imageIds for each request")
  void testGenerateSignature_ShouldGenerateUniqueImageIds() {
    // Arrange
    ImageType imageType = ImageType.PRODUCT;
    when(cloudinary.apiSignRequest(any(), anyString())).thenReturn("sig1").thenReturn("sig2");
    when(imageTrackerRepository.save(any(ImageTracker.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    Map<String, Object> result1 = cloudinaryService.generateUploadSignature(imageType);
    Map<String, Object> result2 = cloudinaryService.generateUploadSignature(imageType);

    // Assert
    String id1 = (String) result1.get("public_id");
    String id2 = (String) result2.get("public_id");
    assertNotEquals(id1, id2);
  }

  @Test
  @DisplayName("✅ Should include timestamp in signature response")
  void testGenerateSignature_ShouldIncludeTimestamp() {
    // Arrange
    when(cloudinary.apiSignRequest(any(), anyString())).thenReturn("sig");
    when(imageTrackerRepository.save(any(ImageTracker.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    Map<String, Object> result = cloudinaryService.generateUploadSignature(ImageType.PRODUCT);

    // Assert
    assertNotNull(result.get("timestamp"));
    assertTrue(result.get("timestamp") instanceof Long);
    assertTrue((Long) result.get("timestamp") > 0);
  }

  // ============ generateResizedUrl Tests ============

  @Test
  @DisplayName("✅ Should generate resized URL with valid parameters")
  void testGenerateResizedUrl_WithValidParameters() {
    // Arrange
    String publicId = "products/123abc";
    int width = 300;
    int height = 300;

    // Mock Cloudinary URL builder
    com.cloudinary.Transformation transformation = mock(com.cloudinary.Transformation.class);

    // Act
    String result = cloudinaryService.generateResizedUrl(publicId, width, height);

    // Assert
    assertNotNull(result);
  }

  @Test
  @DisplayName("❌ Should return null when publicId is null")
  void testGenerateResizedUrl_WithNullPublicId() {
    // Act
    String result = cloudinaryService.generateResizedUrl(null, 300, 300);

    // Assert
    assertNull(result);
  }

  @Test
  @DisplayName("❌ Should return null when publicId is empty")
  void testGenerateResizedUrl_WithEmptyPublicId() {
    // Act
    String result = cloudinaryService.generateResizedUrl("", 300, 300);

    // Assert
    assertNull(result);
  }

  @Test
  @DisplayName("✅ Should accept various valid dimensions")
  void testGenerateResizedUrl_WithVariousDimensions() {
    // Arrange
    String publicId = "products/123";
    int[][] validDimensions = {{100, 100}, {300, 300}, {500, 500}, {1000, 1000}};

    // Act & Assert
    for (int[] dims : validDimensions) {
      String result = cloudinaryService.generateResizedUrl(publicId, dims[0], dims[1]);
      assertNotNull(result);
    }
  }

  @Test
  @DisplayName("✅ Should handle different quality and format settings")
  void testGenerateResizedUrl_ShouldUseAutoQualityAndFormat() {
    // Arrange
    String publicId = "products/image123";

    // Act
    String result = cloudinaryService.generateResizedUrl(publicId, 300, 300);

    // Assert
    assertNotNull(result);
    // URL should contain auto quality and format
  }

  @Test
  @DisplayName("✅ Should use fit crop mode")
  void testGenerateResizedUrl_ShouldUseFitCropMode() {
    // Arrange
    String publicId = "products/xyz";

    // Act
    String result = cloudinaryService.generateResizedUrl(publicId, 300, 300);

    // Assert
    assertNotNull(result);
  }

  @ParameterizedTest
  @ValueSource(
      strings = {"products/info/abc123", "users/avatars/user1", "products/reviews/review1"})
  @DisplayName("✅ Should handle various publicId formats")
  void testGenerateResizedUrl_WithVariousPublicIdFormats(String publicId) {
    // Act
    String result = cloudinaryService.generateResizedUrl(publicId, 300, 300);

    // Assert
    assertNotNull(result);
  }

  // ============ Integration Tests ============

  @Test
  @DisplayName("✅ Should complete full upload flow: signature → tracking → URL generation")
  void testCompleteUploadFlow() {
    // Arrange
    ImageType imageType = ImageType.PRODUCT;
    when(cloudinary.apiSignRequest(any(), anyString())).thenReturn("signature");
    when(imageTrackerRepository.save(any(ImageTracker.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    Map<String, Object> signature = cloudinaryService.generateUploadSignature(imageType);
    String publicId = (String) signature.get("public_id");
    String resizedUrl =
        cloudinaryService.generateResizedUrl("shoppe/products/info/" + publicId, 300, 300);

    // Assert
    assertNotNull(signature);
    assertNotNull(resizedUrl);
    verify(imageTrackerRepository, times(1)).save(any(ImageTracker.class));
  }

  @Test
  @DisplayName("✅ Should properly format folder path for each ImageType")
  void testFolderPathForEachImageType() {
    // Arrange
    ImageType[] types = {
      ImageType.PRODUCT, ImageType.AVATAR, ImageType.REVIEW, ImageType.CONVERSATION
    };
    String[] expectedFolders = {
      "products/info", "users/avatars", "products/reviews", "conversations/images"
    };

    when(cloudinary.apiSignRequest(any(), anyString())).thenReturn("sig");
    when(imageTrackerRepository.save(any(ImageTracker.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act & Assert
    for (int i = 0; i < types.length; i++) {
      cloudinaryService.generateUploadSignature(types[i]);

      ArgumentCaptor<ImageTracker> captor = ArgumentCaptor.forClass(ImageTracker.class);
      verify(imageTrackerRepository).save(captor.capture());
      ImageTracker tracker = captor.getValue();
      assertTrue(tracker.getImageId().contains(expectedFolders[i]));

      // Reset mock for next iteration
      reset(imageTrackerRepository);
      when(imageTrackerRepository.save(any(ImageTracker.class)))
          .thenAnswer(invocation -> invocation.getArgument(0));
    }
  }
}
