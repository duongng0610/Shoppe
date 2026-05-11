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
    ARGUMENT_TYPE_INVALID("Argument type is invalid", 997, HttpStatus.BAD_REQUEST),
    NOT_CAST_TYPE("Not cast type in jpa", 998, HttpStatus.INTERNAL_SERVER_ERROR),
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
            "This order can not be shipped because it was not approved", 1034, HttpStatus.BAD_REQUEST),
    NOT_EXISTED_TRANSACTION("Tracsaction is not existed", 1040, HttpStatus.BAD_REQUEST),
    UNABLE_APPROVE_PRODUCT("Unable approve product", 1041, HttpStatus.BAD_REQUEST),
    UNABLE_BAN_PRODUCT("Unable ban product", 1042, HttpStatus.BAD_REQUEST),
    UNABLE_UNLOCK_PRODUCT("Unable unlock product", 1043, HttpStatus.BAD_REQUEST),
    UNABLE_HIDDEN_PRODUCT("Unable hidden product", 1044, HttpStatus.BAD_REQUEST),
    UNABLE_UNHIDDEN_PRODUCT("Unable unhidden product", 1045, HttpStatus.BAD_REQUEST),
    ORDER_MUST_NOT_PAY("Order must not pay", 1046, HttpStatus.BAD_REQUEST),
    ORDER_TOTAL_PRICE_CHANGED("Order total price changed", 1047, HttpStatus.BAD_REQUEST),
    PRODUCT_NOT_ACTIVE("Product is not active", 1048, HttpStatus.BAD_REQUEST),
    UNABLE_CREATE_ORDER("Unable to create order", 2001, HttpStatus.BAD_REQUEST),
    UNABLE_APPROVE_ORDER("Unable to approve order", 2002, HttpStatus.BAD_REQUEST),
    UNABLE_PAY_ORDER("Unable to pay order", 2003, HttpStatus.BAD_REQUEST),
    UNABLE_SHIP_ORDER("Unable to ship order", 2004, HttpStatus.BAD_REQUEST),
    UNABLE_CANCEL_ORDER("Unable to cancel order", 2005, HttpStatus.BAD_REQUEST),
    UNABLE_CANCEL_ORDER_BY_CLIENT("Client unable to cancel order", 2006, HttpStatus.BAD_REQUEST),
    UNABLE_CANCEL_ORDER_BY_SELLER("Seller unable to cancel order", 2007, HttpStatus.BAD_REQUEST),
    UNABLE_COMPLETE_ORDER("Unable to complete order", 2008, HttpStatus.BAD_REQUEST),
    UNABLE_REQUEST_REFUND("Unable to request refund", 2009, HttpStatus.BAD_REQUEST),
    UNABLE_APPROVE_REFUND("Unable to approve refund", 2010, HttpStatus.BAD_REQUEST),
    UNABLE_REJECT_REFUND("Unable to reject refund", 2011, HttpStatus.BAD_REQUEST),
    UNABLE_RETURN_ORDER("Unable to return order", 2012, HttpStatus.BAD_REQUEST),
    UNABLE_UPDATE_ORDER("Unable to update order", 2013, HttpStatus.BAD_REQUEST),
    NOT_EXIST_ADDRESS("Address is not existed", 2014, HttpStatus.BAD_REQUEST),
    NOT_EXIST_SHOP("Shop  is not existed", 2015, HttpStatus.CONFLICT),
    ERROR_EXTERNAL_API("External api is error", 2016, HttpStatus.INTERNAL_SERVER_ERROR),
    NOT_RESERVE_AVAILABLE("Không còn chỗ trống", 2017, HttpStatus.BAD_REQUEST),
    RESERVATION_CONFLICT("Reservation conflict", 2018, HttpStatus.INTERNAL_SERVER_ERROR),
    UNABLE_GET_ORDER_SHIPPING("Unable to view order shipping", 2019, HttpStatus.FORBIDDEN),
    SHIPMENT_INVALID_REQUEST("Invalid shipment information", 2020, HttpStatus.BAD_REQUEST),
    CREATE_SHOP_VALID_REQUEST("Invalid create shop request information", 2021, HttpStatus.BAD_REQUEST),
    UNABLE_DELETE_CATEGORY("Unable to delete category", 2022, HttpStatus.BAD_REQUEST),
    ;

    String message;
    int code;
    HttpStatus httpStatus;
}
