package com.e_cormerce.shoppe.exception;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.enums.ErrorCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
        return ResponseEntity.status(ErrorCode.UNCATEGORIZED.getHttpStatus())
                .body(
                        ApiResponse.builder()
                                .success(false)
                                .message(ErrorCode.UNCATEGORIZED.getMessage())
                                .build());
    }

    /**
     * Exception lỗi
     *
     * @param e
     * @return
     */
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ApiResponse> AppException(AppException e) {
        return ResponseEntity.status(e.getErrorCode().getHttpStatus())
                .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
    }

    /**
     * Exception validate
     *
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse> ValidateException(MethodArgumentNotValidException e) {
        // message
        return ResponseEntity.badRequest().body(ApiResponse.builder().success(false).message(e.getBindingResult()
                .getFieldError()
                .getDefaultMessage()).code(400).build());
    }
}
