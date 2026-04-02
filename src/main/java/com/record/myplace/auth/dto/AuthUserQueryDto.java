package com.record.myplace.auth.dto;

import java.time.LocalDate;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "인증용 사용자 조회 DTO")
public class AuthUserQueryDto {

    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String email;

    @Schema(description = "암호화된 비밀번호", example = "$2a$10$...")
    private String password;

    @Schema(description = "사용자 이름", example = "이준민")
    private String username;

    @Schema(description = "닉네임", example = "준민")
    private String nickname;

    @Schema(description = "생년월일", example = "1995-05-10")
    private LocalDate birthDate;

    @Schema(description = "성별", example = "M")
    private String gender;

    @Schema(description = "자기소개", example = "맛집 탐방을 좋아합니다.")
    private String bio;
}