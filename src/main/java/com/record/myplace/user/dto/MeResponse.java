package com.record.myplace.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MeResponse {
	private String useremail;
	private String nickname;
	private String bio;
}
