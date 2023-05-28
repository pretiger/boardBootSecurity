package com.board.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.board.security.domain.User;
import com.board.security.mapper.UserDao;

@Service
public class UserService {

	@Autowired
	UserDao userDao;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	public User loginchk(User user) {
		return userDao.loginchk(user);
	}
	
	public void insert(User user) {
		String bcPassword = encoder.encode(user.getPassword());
		user.setPassword(bcPassword);
		userDao.insert(user);
	}
	
	public User login(String username) {
		return userDao.login(username);
	}
	
	public void update(User user) {
		String bcPassword = encoder.encode(user.getPassword());
		user.setPassword(bcPassword);
		userDao.update(user);
	}
	
}
