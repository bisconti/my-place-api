package com.record.myplace.auth.service;

import com.record.myplace.auth.dto.LoginRequest;
import com.record.myplace.auth.dto.LoginResponse;
import com.record.myplace.auth.dto.SignUpRequest;

public interface AuthService {

	LoginResponse login(LoginRequest request);
	
	String refresh(String refreshTokenRaw);

	boolean checkEmailDuplication(String email);

	void signUp(SignUpRequest req);

	void sendPasswordResetEmail(String email);

	void resetPassword(String token, String newPassword);

	void validateResetPasswordToken(String token);

}
