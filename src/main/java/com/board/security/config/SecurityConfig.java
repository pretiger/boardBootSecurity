package com.board.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.board.security.config.authentication.LoginFailureHandler;
import com.board.security.config.authentication.LoginSuccessHandler;
import com.board.security.config.authentication.PrincipalDetailService;
import com.board.security.config.authentication.UserDeniedHandler;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	PrincipalDetailService principalDetailService;
	
	@Autowired
	LoginSuccessHandler loginSuccessHandler;
	
	@Autowired
	LoginFailureHandler loginFailureHandler;
	
	@Autowired
	UserDeniedHandler userDeniedHandler;
	
	@Bean
	public BCryptPasswordEncoder encoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()   //csrf 토큰 비활성화 (테스트시 걸어두는것이 좋음
			.authorizeRequests()
				.antMatchers("/","/js/**","/css/**","/image/**","/auth/**")
				.permitAll()
				.antMatchers("/admin/**")
				.hasRole("ADMIN")
				.anyRequest()
				.authenticated()
			.and()
				.formLogin()
				.loginPage("/auth/loginForm") //login페이지에 해당하는 loginForm을 호출
				.loginProcessingUrl("/auth/login") //loginForm에서 post로 보내온 데이터에 대한 mapping, data는 json type으로 보내면 인식 않되니 주의
				.successHandler(loginSuccessHandler) //PrincipalDetailService에서 제대로 username을 처리한 후 정의한 loginSuccessHandler 호출
				.failureHandler(loginFailureHandler) //비정상적인 login이 되었을 경우 loginFailureHandler 호출
			.and()
				.exceptionHandling()
				.accessDeniedHandler(userDeniedHandler);
	}
}
