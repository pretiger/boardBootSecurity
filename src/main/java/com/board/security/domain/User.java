package com.board.security.domain;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
	private int id;
	private String username;
	private String password;
	private String email;
	private String role;
	private String oauth;
	private Date createDate;
}
