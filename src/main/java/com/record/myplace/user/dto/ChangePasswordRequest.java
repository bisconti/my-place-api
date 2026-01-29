package com.record.myplace.user.dto;

import jakarta.validation.constraints.NotBlank;

public class ChangePasswordRequest {
	@NotBlank
	private String currentPassword;
	
	@NotBlank
	private String newPassword;
}
