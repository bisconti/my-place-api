package com.record.myplace.user.service;

public interface UserService {

	Object getMe(String name);

	Object updateMe(String name, String nickname, String bio);

    
}
