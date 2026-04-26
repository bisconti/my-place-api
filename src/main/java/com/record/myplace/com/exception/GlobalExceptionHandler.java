package com.record.myplace.com.exception;

import java.time.LocalDateTime;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.record.myplace.auth.exception.UnauthorizedException;
import com.record.myplace.com.dto.ApiErrorResponse;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgument(IllegalArgumentException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.BAD_REQUEST, "BAD_REQUEST", ex.getMessage(), request, ex, false);
    }

    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<ApiErrorResponse> handleSecurity(SecurityException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "SECURITY_ERROR", ex.getMessage(), request, ex, false);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorized(UnauthorizedException ex, HttpServletRequest request) {
        return buildErrorResponse(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", ex.getMessage(), request, ex, false);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("요청 값이 올바르지 않습니다.");

        return buildErrorResponse(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", message, request, ex, false);
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<ApiErrorResponse> handleResponseStatus(ResponseStatusException ex, HttpServletRequest request) {
        HttpStatus status = HttpStatus.valueOf(ex.getStatusCode().value());
        String message = ex.getReason() != null ? ex.getReason() : "요청 처리 중 오류가 발생했습니다.";
        return buildErrorResponse(status, "RESPONSE_STATUS_ERROR", message, request, ex, false);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleException(Exception ex, HttpServletRequest request) {
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "INTERNAL_SERVER_ERROR",
                "서버 오류가 발생했습니다.",
                request,
                ex,
                true
        );
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(
            HttpStatus status,
            String code,
            String message,
            HttpServletRequest request,
            Exception ex,
            boolean withStackTrace
    ) {
        String traceId = resolveTraceId(request);
        String path = request.getRequestURI();

        if (withStackTrace) {
            log.error("[traceId={}] {} {} -> {} {}", traceId, request.getMethod(), path, status.value(), message, ex);
        } else {
            log.warn("[traceId={}] {} {} -> {} {} ({})",
                    traceId, request.getMethod(), path, status.value(), message, ex.getClass().getSimpleName());
        }

        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(status.value())
                .code(code)
                .message(message)
                .path(path)
                .traceId(traceId)
                .timestamp(LocalDateTime.now())
                .build();

        return ResponseEntity.status(status).body(response);
    }

    private String resolveTraceId(HttpServletRequest request) {
        Object traceId = request.getAttribute("traceId");
        return traceId instanceof String ? (String) traceId : "unknown";
    }
}
