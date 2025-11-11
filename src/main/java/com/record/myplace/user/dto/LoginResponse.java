package com.record.myplace.user.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

/**
 * 로그인 성공 시 사용자에게 응답할 데이터 구조입니다.
 */
@Getter
@Setter
@Builder
public class LoginResponse {
    
    // 클라이언트에 전달할 사용자 정보
    private UserDto user;
    
    // JWT 토큰 (프론트엔드에서 요구하는 key)
    private String token; 
    
    // 메시지
    private String message;
}
