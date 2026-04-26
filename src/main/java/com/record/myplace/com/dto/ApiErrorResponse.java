package com.record.myplace.com.dto;

import java.time.LocalDateTime;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(description = "공통 API 에러 응답 DTO")
public class ApiErrorResponse {

    @Schema(description = "HTTP 상태 코드", example = "400")
    private final int status;

    @Schema(description = "애플리케이션 에러 코드", example = "BAD_REQUEST")
    private final String code;

    @Schema(description = "사용자에게 보여줄 에러 메시지", example = "이메일 주소를 입력해주세요.")
    private final String message;

    @Schema(description = "요청 경로", example = "/auth/login")
    private final String path;

    @Schema(description = "에러 추적 ID", example = "9b2f3ec4d53a4e59")
    private final String traceId;

    @Schema(description = "에러 발생 시각", example = "2026-04-23T15:20:30")
    private final LocalDateTime timestamp;
}
