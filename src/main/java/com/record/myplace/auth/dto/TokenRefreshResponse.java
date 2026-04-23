package com.record.myplace.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "토큰 재발급 응답 DTO")
public class TokenRefreshResponse {

    @Schema(description = "새 access token", example = "eyJhbGciOiJIUzI1NiJ9.access")
    private String accessToken;

    @Schema(description = "새 refresh token", example = "eyJhbGciOiJIUzI1NiJ9.refresh")
    private String refreshToken;
}
