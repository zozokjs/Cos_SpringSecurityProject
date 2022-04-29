package com.cos.security1.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
//@EnableWebSecurity 이걸 쓰면 스프링 시큐리티 필터가 스프링의 필터 체인에 등록됨
@EnableWebSecurity 
public class SecurityConfig extends WebSecurityConfigurerAdapter{

	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.authorizeRequests()
				.antMatchers("/user/**").authenticated() //  /user/~의 주소로 들어오면 반드시 인증을 거쳐야 하도록 세팅
				.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
				.anyRequest().permitAll() // 그 외의 요청은 권한 허용
				.and()
				.formLogin()
				.loginPage("/login"); //권한 요청이 올 때는 반드시 /login 페이지가 요청됨
		
	
	}
}
