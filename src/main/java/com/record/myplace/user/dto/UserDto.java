package com.record.myplace.user.dto;

import com.record.myplace.user.User;
import lombok.Getter;

/**
 * 사용자 엔티티에서 민감 정보를 제외하고 클라이언트에 노출할 정보만 담는 DTO입니다.
 */
@Getter
public class UserDto {
    private final String useremail;
	private final String username;

    // 엔티티를 DTO로 변환하는 생성자
    public UserDto(User user) {
        this.useremail = user.getEmail();
        this.username = user.getUsername();
    }
}
