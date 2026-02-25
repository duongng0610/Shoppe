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
    UNAUTHORIZED("You don't have permission", 1002, HttpStatus.FORBIDDEN);

    String message;

    int code;

    HttpStatus httpStatus;

}
