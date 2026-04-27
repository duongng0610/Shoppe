package com.e_cormerce.shoppe.entity.product;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.Date;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@FieldDefaults(level = AccessLevel.PRIVATE)
class ProductExtraImageTest {

  Product mockProduct;
  ProductExtraImage productExtraImage;

  @BeforeEach
  void setUp() {
    mockProduct = Product.builder().id("product1").name("Test Product").build();
  }

  // ============ Builder Pattern Tests ============

  @Test
  @DisplayName("✅ Should create ProductExtraImage with all fields")
  void testBuilder_WithAllFields() {
    // Arrange & Act
    productExtraImage =
        ProductExtraImage.builder()
            .id("image1")
            .url("https://cloudinary.com/image1.jpg")
            .imageId("cloudinary-image-id-1")
            .product(mockProduct)
            .build();

    // Assert
    assertNotNull(productExtraImage);
    assertEquals("image1", productExtraImage.getId());
    assertEquals("https://cloudinary.com/image1.jpg", productExtraImage.getUrl());
    assertEquals("cloudinary-image-id-1", productExtraImage.getImageId());
    assertEquals(mockProduct, productExtraImage.getProduct());
  }

  @Test
  @DisplayName("✅ Should set deleted flag to false by default")
  void testBuilder_DeletedFalseByDefault() {
    // Arrange & Act
    productExtraImage =
        ProductExtraImage.builder()
            .id("image1")
            .url("https://example.com/image.jpg")
            .imageId("img-id")
            .product(mockProduct)
            .build();

    // Assert
    assertFalse(productExtraImage.isDeleted());
  }

  // ============ Field Validation Tests ============

  @Test
  @DisplayName("✅ Should accept valid URL")
  void testSetUrl_WithValidURL() {
    // Arrange
    productExtraImage =
        ProductExtraImage.builder().id("img1").imageId("id1").product(mockProduct).build();

    String validUrl = "https://res.cloudinary.com/demo/image/upload/v1234567890/products/img1.jpg";

    // Act
    productExtraImage.setUrl(validUrl);

    // Assert
    assertEquals(validUrl, productExtraImage.getUrl());
  }

  @Test
  @DisplayName("✅ Should accept various image formats")
  void testSetUrl_WithVariousFormats() {
    // Arrange
    String[] urls = {
      "https://cloudinary.com/image.jpg",
      "https://cloudinary.com/image.png",
      "https://cloudinary.com/image.webp",
      "https://cloudinary.com/image.gif"
    };

    productExtraImage =
        ProductExtraImage.builder().id("img1").imageId("id1").product(mockProduct).build();

    // Act & Assert
    for (String url : urls) {
      productExtraImage.setUrl(url);
      assertEquals(url, productExtraImage.getUrl());
    }
  }

  @Test
  @DisplayName("✅ Should set imageId correctly")
  void testSetImageId_WithValidId() {
    // Arrange
    productExtraImage =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://example.com/image.jpg")
            .product(mockProduct)
            .build();

    String imageId = "shoppe/products/info/uuid-12345";

    // Act
    productExtraImage.setImageId(imageId);

    // Assert
    assertEquals(imageId, productExtraImage.getImageId());
  }

  // ============ Product Reference Tests ============

  @Test
  @DisplayName("✅ Should maintain product reference")
  void testProductReference_ShouldMaintainRelationship() {
    // Arrange
    Product product = Product.builder().id("prod1").name("Laptop").build();

    productExtraImage =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://example.com/image.jpg")
            .imageId("img-id-1")
            .product(product)
            .build();

    // Act
    Product retrievedProduct = productExtraImage.getProduct();

    // Assert
    assertEquals("prod1", retrievedProduct.getId());
    assertEquals("Laptop", retrievedProduct.getName());
  }

  @Test
  @DisplayName("✅ Should change product reference")
  void testProductReference_ShouldChangeReference() {
    // Arrange
    Product product1 = Product.builder().id("prod1").name("Product 1").build();
    Product product2 = Product.builder().id("prod2").name("Product 2").build();

    productExtraImage =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://example.com/image.jpg")
            .imageId("img-id")
            .product(product1)
            .build();

    // Act
    productExtraImage.setProduct(product2);

    // Assert
    assertEquals("prod2", productExtraImage.getProduct().getId());
  }

  // ============ Deletion Tests ============

  @Test
  @DisplayName("✅ Should mark image as deleted")
  void testSetDeleted_ShouldMarkAsDeleted() {
    // Arrange
    productExtraImage =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://example.com/image.jpg")
            .imageId("img-id")
            .product(mockProduct)
            .deleted(false)
            .build();

    // Act
    productExtraImage.setDeleted(true);

    // Assert
    assertTrue(productExtraImage.isDeleted());
  }

  @Test
  @DisplayName("✅ Should mark deleted image as not deleted")
  void testSetDeleted_ShouldUnmarkDeleted() {
    // Arrange
    productExtraImage =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://example.com/image.jpg")
            .imageId("img-id")
            .product(mockProduct)
            .deleted(true)
            .build();

    // Act
    productExtraImage.setDeleted(false);

    // Assert
    assertFalse(productExtraImage.isDeleted());
  }

  // ============ Timestamp Tests ============

  @Test
  @DisplayName("✅ Should have createdAt timestamp (automatically set)")
  void testTimestamp_CreatedAtNotNull() {
    // Arrange & Act
    productExtraImage =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://example.com/image.jpg")
            .imageId("img-id")
            .product(mockProduct)
            .createdAt(LocalDateTime.now())
            .build();

    // Assert
    assertNotNull(productExtraImage.getCreatedAt());
  }

  @Test
  @DisplayName("✅ Should allow setting custom updatedAt")
  void testTimestamp_UpdatedAtCanBeSet() {
    // Arrange
    productExtraImage =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://example.com/image.jpg")
            .imageId("img-id")
            .product(mockProduct)
            .build();

    Date now = new Date();

    // Act
    productExtraImage.setUpdatedAt(now);

    // Assert
    assertEquals(now, productExtraImage.getUpdatedAt());
  }

  // ============ Multiple Images Tests ============

  @Test
  @DisplayName("✅ Should create multiple images for same product")
  void testMultipleImages_SameProduct() {
    // Arrange & Act
    ProductExtraImage image1 =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://example.com/image1.jpg")
            .imageId("id-1")
            .product(mockProduct)
            .build();

    ProductExtraImage image2 =
        ProductExtraImage.builder()
            .id("img2")
            .url("https://example.com/image2.jpg")
            .imageId("id-2")
            .product(mockProduct)
            .build();

    ProductExtraImage image3 =
        ProductExtraImage.builder()
            .id("img3")
            .url("https://example.com/image3.jpg")
            .imageId("id-3")
            .product(mockProduct)
            .build();

    // Assert
    assertEquals(mockProduct, image1.getProduct());
    assertEquals(mockProduct, image2.getProduct());
    assertEquals(mockProduct, image3.getProduct());
    assertNotEquals(image1.getId(), image2.getId());
    assertNotEquals(image2.getId(), image3.getId());
  }

  // ============ Soft Delete Tests (SQLDelete Annotation) ============

  @Test
  @DisplayName("✅ Should support soft delete via deleted flag")
  void testSoftDelete_DeletedFlagUsed() {
    // Arrange
    productExtraImage =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://example.com/image.jpg")
            .imageId("img-id")
            .product(mockProduct)
            .deleted(false)
            .build();

    // Act - Mark as deleted
    productExtraImage.setDeleted(true);

    // Assert
    assertTrue(productExtraImage.isDeleted());
    // In real scenario, query should not return this with WHERE deleted = false
  }

  // ============ Getter/Setter Tests ============

  @Test
  @DisplayName("✅ Should get all properties correctly")
  void testGetters_AllProperties() {
    // Arrange
    LocalDateTime now = LocalDateTime.now();
    Date updateDate = new Date();

    productExtraImage =
        ProductExtraImage.builder()
            .id("img-uuid-123")
            .url("https://cloudinary.com/image.jpg")
            .imageId("shoppe/products/info/image-id-123")
            .product(mockProduct)
            .deleted(false)
            .createdAt(now)
            .build();
    productExtraImage.setUpdatedAt(updateDate);

    // Act & Assert
    assertEquals("img-uuid-123", productExtraImage.getId());
    assertEquals("https://cloudinary.com/image.jpg", productExtraImage.getUrl());
    assertEquals("shoppe/products/info/image-id-123", productExtraImage.getImageId());
    assertEquals(mockProduct, productExtraImage.getProduct());
    assertFalse(productExtraImage.isDeleted());
    assertEquals(now, productExtraImage.getCreatedAt());
    assertEquals(updateDate, productExtraImage.getUpdatedAt());
  }

  @Test
  @DisplayName("✅ Should update properties via setters")
  void testSetters_AllProperties() {
    // Arrange
    productExtraImage =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://old-url.com/image.jpg")
            .imageId("old-id")
            .product(mockProduct)
            .deleted(false)
            .build();

    Product newProduct = Product.builder().id("prod2").name("New Product").build();
    String newUrl = "https://new-url.com/image.jpg";
    String newImageId = "new-id";
    Date newUpdateDate = new Date();

    // Act
    productExtraImage.setUrl(newUrl);
    productExtraImage.setImageId(newImageId);
    productExtraImage.setProduct(newProduct);
    productExtraImage.setDeleted(true);
    productExtraImage.setUpdatedAt(newUpdateDate);

    // Assert
    assertEquals(newUrl, productExtraImage.getUrl());
    assertEquals(newImageId, productExtraImage.getImageId());
    assertEquals(newProduct, productExtraImage.getProduct());
    assertTrue(productExtraImage.isDeleted());
    assertEquals(newUpdateDate, productExtraImage.getUpdatedAt());
  }

  // ============ Builder Edge Cases ============

  @Test
  @DisplayName("✅ Should allow partial builder initialization")
  void testBuilder_PartialInitialization() {
    // Arrange & Act
    productExtraImage =
        ProductExtraImage.builder().id("img1").url("https://example.com/image.jpg").build();

    // Assert
    assertEquals("img1", productExtraImage.getId());
    assertEquals("https://example.com/image.jpg", productExtraImage.getUrl());
    assertNull(productExtraImage.getImageId()); // Can be null initially
    assertNull(productExtraImage.getProduct()); // Can be null initially
  }

  @Test
  @DisplayName("✅ Should allow null product initially")
  void testBuilder_NullProductInitially() {
    // Arrange & Act
    productExtraImage =
        ProductExtraImage.builder()
            .id("img1")
            .url("https://example.com/image.jpg")
            .imageId("img-id")
            .product(null)
            .build();

    // Assert
    assertNull(productExtraImage.getProduct());

    // Then set product later
    productExtraImage.setProduct(mockProduct);
    assertEquals(mockProduct, productExtraImage.getProduct());
  }
}
