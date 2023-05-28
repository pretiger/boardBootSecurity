package com.board.security.controller;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.board.security.config.authentication.PrincipalDetail;
import com.board.security.config.authentication.PrincipalDetailService;
import com.board.security.domain.ResponseDTO;
import com.board.security.domain.User;
import com.board.security.service.UserService;

@Controller
public class UserController {

	private final static Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	BCryptPasswordEncoder encoder;
	
	@Autowired
	HttpSession session;
	
	@Autowired
	PrincipalDetailService authService;
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		return "user/loginForm";
	}
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		return "user/joinForm";
	}
	
	@ResponseBody
	@PostMapping("/auth/insert")
	public ResponseDTO<String> insert(@RequestBody User user){
		userService.insert(user);
		UserDetails principal = authService.loadUserByUsername(user.getUsername());
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseDTO<String>(HttpStatus.OK.value(), "user insert seccess!");
	}
	
	@ResponseBody
	@GetMapping("/logout")
	public ResponseDTO<String> logout(HttpSession session) {
		session.invalidate();
		return new ResponseDTO<String>(HttpStatus.OK.value(), "logout!");
	}
	
	@GetMapping("/user/userInfo")
	public String userInfo(Model model, Authentication auth) {
		PrincipalDetail principal = (PrincipalDetail)auth.getPrincipal();
		model.addAttribute("dto", userService.login(principal.getUsername()));
		return "user/detailForm";
	}
	
	@GetMapping("/auth/denied")
	public String denied(Model model) {
		model.addAttribute("message", "You has not a role!");
		return "user/deniedForm";
	}
	
	@ResponseBody
	@GetMapping("/auth/loginSuccess")
	public ResponseDTO<String> loginSuccess(Authentication auth) {
		PrincipalDetail principal = (PrincipalDetail)auth.getPrincipal();
		return new ResponseDTO<String>(HttpStatus.OK.value(), principal.getUsername());
	}
	
	@ResponseBody
	@GetMapping("/auth/loginError")
	public ResponseDTO<String> loginError() {
		return new ResponseDTO<String>(HttpStatus.NOT_FOUND.value(), "Username or Password mismatch!");
	}
	
	@GetMapping("/admin/managed")
	public String managed(Model model) {
		model.addAttribute("message", "Admin Managed Form");
		return "user/managedForm";
	}
	
	@ResponseBody
	@PutMapping("/user/update")
	public ResponseDTO<String> update(@RequestBody User user) {
		userService.update(user);
		UserDetails principal = authService.loadUserByUsername(user.getUsername());
		Authentication authentication = new UsernamePasswordAuthenticationToken(principal, principal.getPassword(), principal.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseDTO<String>(HttpStatus.OK.value(), "User update Success!");
	}
	
}
