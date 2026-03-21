package com.e_cormerce.shoppe.exception;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.transaction.TransactionalException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

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

    /**
     * Use empty field of request object.
     */
    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity<ApiResponse> UnrecognizedPropertyException(UnrecognizedPropertyException e) {
        return ResponseEntity.status(ErrorCode.USE_EMPTY_FIELD.getHttpStatus()).body(ApiResponse.builder().success(false).message(e.getMessage()).build());
    }

    /**
     * Empty request param / request part
     */
    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<ApiResponse> MissingServletRequestPartException(MissingServletRequestPartException e) {
        return ResponseEntity.status(ErrorCode.MISSING_REQUIRED_PARAMS.getHttpStatus())
                .body(ApiResponse.builder()
                        .success(false)
                        .message(ErrorCode.MISSING_REQUIRED_PARAMS.getMessage())
                        .build());
    }

    /**
     * Authorization denided
     */
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse> AuthorizationDeniedException(AuthorizationDeniedException e) {
        return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getHttpStatus()).body(ApiResponse.builder().success(false).message(e.getMessage()).build());
    }

    /**
     * Timeout
     */
    @ExceptionHandler(TransactionalException.class)
    public ResponseEntity<ApiResponse> TransactionalException(TransactionalException e) {
        return ResponseEntity.status(ErrorCode.TIME_OUT_REQUEST.getHttpStatus()).body(ApiResponse.builder().success(false).message(e.getMessage()).build());
    }
}
