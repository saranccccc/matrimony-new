package com.matrimony.common;

import com.matrimony.common.exception.CustomException;
import com.matrimony.common.exception.ErrorCode;
import com.matrimony.common.exception.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex, HttpServletRequest request) {
        ErrorCode errorCode = ex.getErrorCode();
        log.error("Business Exception: {}", errorCode.name());
        ErrorResponse response = ErrorResponse.builder().timestamp(java.time.LocalDateTime.now()).status(errorCode.getHttpStatus().value()).errorCode(errorCode.name()).message(errorCode.getMessage()).path(request.getRequestURI()).build();
        return ResponseEntity.status(errorCode.getHttpStatus()).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled Exception: ", ex);
        ErrorResponse response = ErrorResponse.builder().timestamp(java.time.LocalDateTime.now()).status(500).errorCode(ErrorCode.INTERNAL_SERVER_ERROR.name()).message(ErrorCode.INTERNAL_SERVER_ERROR.getMessage()).path(request.getRequestURI()).build();
        return ResponseEntity.status(500).body(response);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<Map<String,String>> handleMaxUpload(MaxUploadSizeExceededException ex) {
        log.error("handleMaxUpload Exception: ", ex);
        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(Map.of("errorCode", "FILE_TOO_LARGE", "message", "Uploaded file exceeds the configured upload limit."));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponse> handleAuthorizationDeniedException(AuthorizationDeniedException ex) {
        ErrorResponse response = ErrorResponse.builder().timestamp(java.time.LocalDateTime.now()).status(HttpStatus.FORBIDDEN.value()).errorCode(HttpStatus.FORBIDDEN.name()).message("Access denied: " + ex.getMessage()).build();
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(response);

    }

}