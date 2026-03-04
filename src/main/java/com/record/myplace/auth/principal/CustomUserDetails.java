package com.record.myplace.auth.principal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.record.myplace.user.entity.User;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Collection;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {
	
	private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 권한 안쓰면 null 가능
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
	}
	
	public String getEmail() {
		return user.getEmail();
	}
}
