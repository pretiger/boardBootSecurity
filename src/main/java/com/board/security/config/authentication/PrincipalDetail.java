package com.board.security.config.authentication;

import java.util.ArrayList;
import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.board.security.domain.User;

import lombok.Getter;

@Getter
public class PrincipalDetail implements UserDetails {

	private static final Logger logger = LoggerFactory.getLogger(PrincipalDetail.class);
	
	private User user;
	
	public PrincipalDetail(User user) {
		this.user = user;
	}

	@Override
	public String getPassword() {
		return user.getPassword();
	}

	@Override
	public String getUsername() {
		return user.getUsername();
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
	public Collection<? extends GrantedAuthority> getAuthorities() {
		Collection<GrantedAuthority> collections = new ArrayList<>();
//		collections.add(new GrantedAuthority() {
//			
//			@Override
//			public String getAuthority() {
//				return user.getRole();
//			}
//		});
		collections.add(()->{return user.getRole();});
		return collections;
	}
	
}
