package com.record.myplace.user.dto;

import lombok.Getter;

// Lombok @Getter를 사용하여 JSON 요청 필드에 접근합니다.
@Getter
public class LoginRequest {
    private String email;
    private String password;
}
