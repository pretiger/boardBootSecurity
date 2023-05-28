package com.board.security.mapper;

import org.apache.ibatis.annotations.Mapper;

import com.board.security.domain.User;

@Mapper
public interface UserDao {
	public User loginchk(User user);
	public void insert(User user);
	public User login(String username);
	public void update(User user);
}
