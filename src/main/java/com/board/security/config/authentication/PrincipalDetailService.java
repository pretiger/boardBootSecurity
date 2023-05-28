package com.board.security.config.authentication;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.board.security.domain.User;
import com.board.security.mapper.UserDao;

@Service
public class PrincipalDetailService implements UserDetailsService {

	private static final Logger logger = LoggerFactory.getLogger(PrincipalDetailService.class);
	
	@Autowired
	UserDao userDao;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userDao.login(username);
		if(user == null) throw new UsernameNotFoundException(username);
		return new PrincipalDetail(user);
	}

}
