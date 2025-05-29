package rw.arsene.erp.v1.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import rw.arsene.erp.v1.dto.ApiResponseDto;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

@ExceptionHandler(ResourceNotFoundException.class)
public ResponseEntity<ApiResponseDto<Object>> handleResourceNotFoundException(
        ResourceNotFoundException ex, WebRequest request) {
    log.error("Resource not found: {}", ex.getMessage());
        
    ApiResponseDto<Object> response = ApiResponseDto.<Object>builder()
            .success(false)
            .message(ex.getMessage())
            .data(null)
            .timestamp(LocalDateTime.now())
            .build();
        
    return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
}

@ExceptionHandler(BusinessException.class)
public ResponseEntity<ApiResponseDto<Object>> handleBusinessException(
        BusinessException ex, WebRequest request) {
    log.error("Business logic error: {}", ex.getMessage());
        
    ApiResponseDto<Object> response = ApiResponseDto.<Object>builder()
            .success(false)
            .message(ex.getMessage())
            .data(null)
            .timestamp(LocalDateTime.now())
            .build();
        
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(DuplicateResourceException.class)
public ResponseEntity<ApiResponseDto<Object>> handleDuplicateResourceException(
        DuplicateResourceException ex, WebRequest request) {
    log.error("Duplicate resource error: {}", ex.getMessage());
        
    ApiResponseDto<Object> response = ApiResponseDto.<Object>builder()
            .success(false)
            .message(ex.getMessage())
            .data(null)
            .timestamp(LocalDateTime.now())
            .build();
        
    return new ResponseEntity<>(response, HttpStatus.CONFLICT);
}

@ExceptionHandler(MethodArgumentNotValidException.class)
public ResponseEntity<ApiResponseDto<Object>> handleValidationExceptions(
        MethodArgumentNotValidException ex) {
    log.error("Validation error: {}", ex.getMessage());
        
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach((error) -> {
        String fieldName = ((FieldError) error).getField();
        String errorMessage = error.getDefaultMessage();
        errors.put(fieldName, errorMessage);
    });

    ApiResponseDto<Object> response = ApiResponseDto.<Object>builder()
            .success(false)
            .message("Validation failed")
            .data(errors)
            .timestamp(LocalDateTime.now())
            .build();
        
    return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
}

@ExceptionHandler(BadCredentialsException.class)
public ResponseEntity<ApiResponseDto<Object>> handleBadCredentialsException(
        BadCredentialsException ex, WebRequest request) {
    log.error("Authentication error: {}", ex.getMessage());
        
    ApiResponseDto<Object> response = ApiResponseDto.<Object>builder()
            .success(false)
            .message("Invalid email or password")
            .data(null)
            .timestamp(LocalDateTime.now())
            .build();
        
    return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
}

@ExceptionHandler(AccessDeniedException.class)
public ResponseEntity<ApiResponseDto<Object>> handleAccessDeniedException(
        AccessDeniedException ex, WebRequest request) {
    log.error("Access denied: {}", ex.getMessage());
        
    ApiResponseDto<Object> response = ApiResponseDto.<Object>builder()
            .success(false)
            .message("Access denied. You don't have permission to access this resource")
            .data(null)
            .timestamp(LocalDateTime.now())
            .build();
        
    return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
}

@ExceptionHandler(Exception.class)
public ResponseEntity<ApiResponseDto<Object>> handleGlobalException(
        Exception ex, WebRequest request) {
    log.error("Unexpected error occurred: ", ex);
        
    ApiResponseDto<Object> response = ApiResponseDto.<Object>builder()
            .success(false)
            .message("An unexpected error occurred. Please try again later")
            .data(null)
            .timestamp(LocalDateTime.now())
            .build();
        
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
}
}