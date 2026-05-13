package com.e_cormerce.shoppe.exception;

import com.e_cormerce.shoppe.dto.response.ApiResponse;
import com.e_cormerce.shoppe.enums.ErrorCode;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import jakarta.transaction.TransactionalException;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.support.MethodArgumentTypeMismatchException;
import org.springframework.orm.jpa.JpaSystemException;
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
        .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
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
  public ResponseEntity<ApiResponse> validateException(MethodArgumentNotValidException e) {

    String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();

    return ResponseEntity.badRequest()
        .body(ApiResponse.builder().success(false).message(message).build());
  }

  /** Use empty field of request object. */
  @ExceptionHandler(UnrecognizedPropertyException.class)
  public ResponseEntity<ApiResponse> handleUnrecognizedPropertyException(
      UnrecognizedPropertyException e) {
    return ResponseEntity.status(ErrorCode.USE_EMPTY_FIELD.getHttpStatus())
        .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
  }

  /** Empty request param / request part */
  @ExceptionHandler(MissingServletRequestPartException.class)
  public ResponseEntity<ApiResponse> handleMissingServletRequestPartException(
      MissingServletRequestPartException e) {
    return ResponseEntity.status(ErrorCode.MISSING_REQUIRED_PARAMS.getHttpStatus())
        .body(
            ApiResponse.builder()
                .success(false)
                .message(ErrorCode.MISSING_REQUIRED_PARAMS.getMessage())
                .build());
  }

  /** Authorization denided */
  @ExceptionHandler(AuthorizationDeniedException.class)
  public ResponseEntity<ApiResponse> handleAuthorizationDeniedException(
      AuthorizationDeniedException e) {
    return ResponseEntity.status(ErrorCode.UNAUTHORIZED.getHttpStatus())
        .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
  }

  /** Timeout */
  @ExceptionHandler(TransactionalException.class)
  public ResponseEntity<ApiResponse> handleTransactionalException(TransactionalException e) {
    return ResponseEntity.status(ErrorCode.TIME_OUT_REQUEST.getHttpStatus())
        .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
  }

  @ExceptionHandler(JpaSystemException.class)
  public ResponseEntity<ApiResponse> handleJpaSystemException(JpaSystemException e) {
    return ResponseEntity.status(ErrorCode.NOT_CAST_TYPE.getHttpStatus())
        .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ResponseEntity<ApiResponse> handleMethodArgumentTypeMismatchException(
      MethodArgumentTypeMismatchException e) {
    return ResponseEntity.status(ErrorCode.ARGUMENT_TYPE_INVALID.getHttpStatus())
        .body(ApiResponse.builder().success(false).message(e.getMessage()).build());
  }
}
