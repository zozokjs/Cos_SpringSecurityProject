package com.cos.security1.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
//@EnableWebSecurity 이걸 쓰면 스프링 시큐리티 필터가 스프링의 필터 체인에 등록됨
@EnableWebSecurity 
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	@Bean	
	//@Bean 해당 메소드의 리턴되는 오브젝트를 IOC에 등록해준다.
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.csrf().disable();
		http.authorizeRequests()
				.antMatchers("/user/**").authenticated() //  /user/~의 주소로 들어오면 반드시 인증을 거쳐야 하도록 세팅. 즉, 인증만 되면 들어갈 수 있음
				.antMatchers("/manager/**").access("hasRole('ROLE_ADMIN') or hasRole('ROLE_MANAGER')")
				.antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
				.anyRequest().permitAll() // 그 외의 요청은 권한 허용
				.and()
				.formLogin()
				.loginPage("/loginForm") //권한 요청이 올 때는 반드시 /login 페이지가 요청됨
				.loginProcessingUrl("/login") // login 페이지가 요청되면 시큐리티가 낚아채서 대신 로그인을 진행한다.
				.defaultSuccessUrl("/"); //로그인 성공 시 이동하는 페이지
	}
}
