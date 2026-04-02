package com.record.myplace.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "비밀번호 변경 요청 DTO")
public class ChangePasswordRequest {

    @NotBlank
    @Schema(description = "현재 비밀번호", example = "oldPassword123!")
    private String currentPassword;

    @NotBlank
    @Schema(description = "새 비밀번호", example = "newPassword123!")
    private String newPassword;
}