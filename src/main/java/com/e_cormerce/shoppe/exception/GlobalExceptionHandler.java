package com.e_cormerce.shoppe.exception;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.enums.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * Exception không thuộc application định nghĩa.
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse> UncategorizedException(Exception e) {
        return ResponseEntity.
                status(ErrorCode.UNCATEGORIZED.getHttpStatus()).
                body(ApiResponse.builder().success(false).message(ErrorCode.UNCATEGORIZED.getMessage()).build());
    }

    /**
     * Exception lỗi
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> AppException(AppException e) {
        return ResponseEntity.
                status(e.getErrorCode().getHttpStatus()).
                body(ApiResponse.builder().success(false).message(e.getMessage()).build());
    }

    /**
     * Exception validate
     *
     * @param e
     * @return
     */

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> ValidateException(MethodArgumentNotValidException e) {
        String enumKey = e.getFieldError().getDefaultMessage();


        ErrorCode errorCode = ErrorCode.UNCATEGORIZED;
        Map<String, Object> attributes = null;

        errorCode = ErrorCode.valueOf(enumKey);

        var constraintViolation = e.getBindingResult()
                    .getAllErrors()
                    .get(1)
                    .unwrap(ConstraintViolation.class);

        attributes =constraintViolation.getConstraintDescriptor().getAttributes();


        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setCode(errorCode.getCode());
        // message
        return ResponseEntity.badRequest().body(apiResponse);
    }
}
