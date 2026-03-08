package com.e_cormerce.shoppe.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Getter
public enum ErrorCode {
  UNCATEGORIZED("Uncategorized", 999, HttpStatus.INTERNAL_SERVER_ERROR),
  UNAUTHENTICATED("Unauthenticated", 1001, HttpStatus.UNAUTHORIZED),
  INVALID_FILE_FORMAT("Invalid File Format", 1003, HttpStatus.BAD_REQUEST),
  UNAUTHORIZED("You don't have permission", 1002, HttpStatus.FORBIDDEN),

  INVALID_ACCOUNT("Your account is invalid", 1003, HttpStatus.BAD_REQUEST),
  INCORRECT_PASSWORD("Password is incorrect", 1004, HttpStatus.BAD_REQUEST),
  EXISTED_ACCOUNT("This account is existed", 1005, HttpStatus.BAD_REQUEST),
  INVALID_ROLE("Your role is invalid", 1006, HttpStatus.BAD_REQUEST),
  INVALID_USERNAME("Your username is invalid", 1007, HttpStatus.BAD_REQUEST),
  INVALID_REFRESH_TOKEN("Your refresh token is invalid", 1008, HttpStatus.BAD_REQUEST),
  NOT_EXISTED_USER("User is not existed", 1009, HttpStatus.BAD_REQUEST),
  INVALID_EMAIL("Your email is invalid", 1010, HttpStatus.BAD_REQUEST),
  INVALID_CREATE_PASSWORD("Your password created is invalid", 1011, HttpStatus.BAD_REQUEST),
  // Create_Product
  INVALID_CREATE_VARIANTS(
      "Your variants and variant images not match", 1012, HttpStatus.BAD_REQUEST),
  UPLOAD_FAILED("Your image can not upload", 1013, HttpStatus.BAD_REQUEST),
  UPLOAD_TIMEOUT("Your image upload too long", 1014, HttpStatus.BAD_REQUEST);

  String message;
  int code;
  HttpStatus httpStatus;
}
