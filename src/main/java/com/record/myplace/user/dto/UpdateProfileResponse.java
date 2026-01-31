package com.record.myplace.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UpdateProfileResponse {
	private String message;
	
	private String useremail;
	
	private String nickname;
	
	private String bio;
}
