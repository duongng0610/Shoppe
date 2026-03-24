package com.e_cormerce.shoppe.util;

import org.springframework.mock.web.MockMultipartFile;

public class CreateMockMultipartFile {
  public static MockMultipartFile createMockImageFile(String name) {
    return new MockMultipartFile(
        name, // name
        "test.png", // originalFilename
        "image/png", // contentType
        "abc".getBytes() // content
        );
  }

  public static MockMultipartFile createMultipartJsonObject(String name, Object object) {
    return new MockMultipartFile(
        name, // name
        "", // originalFilename
        "application/json", // contentType
        ConvertObject.toJson(object).getBytes() // content
        );
  }
}
