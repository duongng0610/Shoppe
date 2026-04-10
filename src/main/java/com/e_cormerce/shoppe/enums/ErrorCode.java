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
  UNAUTHENTICATED("Unauthenticated", 1000, HttpStatus.UNAUTHORIZED),
  INVALID_FILE_FORMAT("Invalid File Format", 1001, HttpStatus.BAD_REQUEST),
  UNAUTHORIZED("You don't have permission", 1002, HttpStatus.FORBIDDEN),

  INVALID_ACCOUNT("Your account is invalid", 1003, HttpStatus.BAD_REQUEST),
  INCORRECT_PASSWORD("Password is incorrect", 1004, HttpStatus.BAD_REQUEST),
  EXISTED_ACCOUNT("This account is existed", 1005, HttpStatus.BAD_REQUEST),
  INVALID_ROLE("Your role is invalid", 1006, HttpStatus.BAD_REQUEST),
  INVALID_USERNAME("Your username is invalid", 1007, HttpStatus.BAD_REQUEST),
  INVALID_REFRESH_TOKEN("Your refresh token is invalid", 1008, HttpStatus.BAD_REQUEST),
  NOT_EXIST_USER("User is not existed", 1009, HttpStatus.BAD_REQUEST),
  NOT_EXIST_SELLER("User is not existed", 1009, HttpStatus.CONFLICT),
  INVALID_EMAIL("Your email is invalid", 1010, HttpStatus.BAD_REQUEST),
  INVALID_CREATE_PASSWORD("Your password created is invalid", 1011, HttpStatus.BAD_REQUEST),
  // Create_Product
  INVALID_CREATE_VARIANTS(
      "Your variants and variant images not match", 1012, HttpStatus.BAD_REQUEST),
  UPLOAD_FAILED("Your image can not upload", 1013, HttpStatus.BAD_REQUEST),
  UPLOAD_TIMEOUT("Your image upload too long", 1014, HttpStatus.BAD_REQUEST),
  INVALID_PARENT_CATEGORY("Your parent category is in invalid", 1015, HttpStatus.BAD_REQUEST),
  NOT_EXISTED_CATEGORY("Category didn't exist", 1016, HttpStatus.BAD_REQUEST),
  EXISTED_CATEGORY("Category didn't exist", 1017, HttpStatus.BAD_REQUEST),
  NOT_EXIST_PRODUCT("Your product is not exist", 1018, HttpStatus.BAD_REQUEST),
  USE_EMPTY_FIELD("Field do not exist", 1019, HttpStatus.BAD_REQUEST),
  CONFLICT_VARIANT_DATA("Invalid variants data", 1020, HttpStatus.BAD_REQUEST),
  CANNOT_CONVERT_OBJECT_TO_JSON("Cannot convert object to json", 1021, HttpStatus.BAD_REQUEST),
  MISSING_REQUIRED_PARAMS("Required parameters are missing", 1022, HttpStatus.BAD_REQUEST),

  TIME_OUT_REQUEST("Request timed out", 1023, HttpStatus.REQUEST_TIMEOUT),
  INVALID_APPROVE_PRODUCT_REQUEST(
      "List products id is null or empty", 1024, HttpStatus.BAD_REQUEST),
  NOT_EXIST_VARIANT("This variant is not exist", 1025, HttpStatus.BAD_REQUEST),
  NOT_EXIST_SHOPPING_CART("Shopping cart is not exist", 1026, HttpStatus.CONFLICT),
  INSUFFICIENT_STOCK("Insufficient stock", 1027, HttpStatus.BAD_REQUEST),
  EXISTED_USERNAME("Username is existed", 1028, HttpStatus.BAD_REQUEST),
  INVALID_CREATE_CONVERSATION("Not chat yourself", 1029, HttpStatus.BAD_REQUEST),
  NOT_EXIST_CONVERSATION("Not exist conversation", 1030, HttpStatus.BAD_REQUEST),
  INVALID_CREATE_MESSAGE("Message must have content", 1031, HttpStatus.BAD_REQUEST),
  NOT_EXISTED_ORDER("Order is not existed", 1032, HttpStatus.BAD_REQUEST),
  NOT_ENOUGH_QUANTITY_FOR_ORDER(
      "Quantity of product in inventory is not enough for order", 1033, HttpStatus.BAD_REQUEST),
  UNABlE_CANCEL_ORDER(
      "Order can not be cancelled because it was approved", 1034, HttpStatus.BAD_REQUEST),
  NOT_EXISTED_TRACKING("Tracking is not existed", 1035, HttpStatus.BAD_REQUEST),
  UNABLE_UPDATE_TRACKING("This tracking is unable to update", 1036, HttpStatus.BAD_REQUEST),
  GEOCODING_FAILED("Failed to geocode address", 1037, HttpStatus.BAD_REQUEST),
  UNABLE_GET_CURRENT_TRACKING("Order is not being delivered", 1038, HttpStatus.BAD_REQUEST),
  INVALID_ORDER_STATUS("Order status is invalid", 1039, HttpStatus.BAD_REQUEST),
  UNABlE_SHIP_ORDER(
      "This order can not be shipped because it was not approved", 1034, HttpStatus.BAD_REQUEST);

  String message;
  int code;
  HttpStatus httpStatus;
}
