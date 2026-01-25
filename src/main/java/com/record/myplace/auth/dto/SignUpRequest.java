package com.record.myplace.auth.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class SignUpRequest {
	private String email;
	private String password;
	private String username;
	private String nickname;
	private String birthDate;
	private String gender;
	private String bio;
}
