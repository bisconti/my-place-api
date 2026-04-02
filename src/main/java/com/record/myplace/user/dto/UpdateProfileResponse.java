package com.record.myplace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "프로필 수정 응답 DTO")
public class UpdateProfileResponse {

    @Schema(description = "사용자 이메일", example = "test@example.com")
    private String email;

    @Schema(description = "닉네임", example = "준민")
    private String nickname;

    @Schema(description = "자기소개", example = "맛집 탐방을 좋아합니다.")
    private String bio;
}