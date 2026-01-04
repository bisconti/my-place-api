package com.record.myplace.user.dto;

import com.record.myplace.user.User;
import lombok.Getter;

@Getter
public class UserDto {
    private final String useremail;
	private final String username;

    public UserDto(User user) {
        this.useremail = user.getEmail();
        this.username = user.getUsername();
    }
}
