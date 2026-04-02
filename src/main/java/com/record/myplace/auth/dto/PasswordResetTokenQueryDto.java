package com.record.myplace.auth.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "비밀번호 재설정 토큰 조회 DTO")
public class PasswordResetTokenQueryDto {

    @Schema(description = "토큰 ID", example = "1")
    private Long id;

    @Schema(description = "재설정 토큰", example = "abc123token")
    private String token;

    @Schema(description = "이메일", example = "test@example.com")
    private String email;

    @Schema(description = "만료 일시", example = "2026-04-02T23:59:59")
    private LocalDateTime expiresAt;

    @Schema(description = "사용 여부", example = "false")
    private Boolean used;
}