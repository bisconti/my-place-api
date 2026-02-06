package com.record.myplace.com.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;

import com.record.myplace.auth.exception.UnauthorizedException;
import com.record.myplace.com.dto.MessageResponse;

import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 잘못된 요청
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException e) {
    	return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
    }

    /**
     * 인증 실패 (로그인 등)
     */
    @ExceptionHandler(SecurityException.class)
    public ResponseEntity<?> handleSecurity(SecurityException e) {
        log.warn("SecurityException: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(Map.of("message", e.getMessage()));
    }
    
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<MessageResponse> handleUnauthorized(UnauthorizedException e) {
        return ResponseEntity.status(401).body(new MessageResponse(e.getMessage()));
    }

    /**
     * @Valid 검증 실패
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException e) {
        String message = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(err -> err.getDefaultMessage())
                .orElse("요청 값이 올바르지 않습니다.");

        return ResponseEntity
                .badRequest()
                .body(Map.of("message", message));
    }
    
    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<MessageResponse> handleResponseStatus(ResponseStatusException e) {
        int status = e.getStatusCode().value();
        String msg = (e.getReason() != null) ? e.getReason() : "요청 처리 중 오류가 발생했습니다.";
        return ResponseEntity.status(status).body(new MessageResponse(msg));
    }
    
    /**
     * 그 외 모든 예외
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<MessageResponse> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body(new MessageResponse("서버 오류가 발생했습니다."));
    }
}
