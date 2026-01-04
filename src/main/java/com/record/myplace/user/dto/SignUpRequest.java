package com.record.myplace.user.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
	private String email;
	private String password;
	private String username;
	private String birthDate;
	private String gender;
}
