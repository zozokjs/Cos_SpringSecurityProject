package com.cos.security1.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.cos.security1.oauth.PrincipalOauth2UserService;

@Configuration
//@EnableWebSecurity 이걸 쓰면 스프링 시큐리티 필터가 스프링의 필터 체인에 등록됨
@EnableWebSecurity 
/**
//@EnableGlobalMethodSecurity의 securedEnabled는 @Secured 어노테이션을 활성화 시킨다.
//@예) Secured("ROLE_ADMIN") 이렇게하면 ROLE_ADMIN 권한을 지닌 사람만 그 주소로 접근 할 수 있음.
//prePostEnabled는 @preAuthorize 어노테이션을 활성화 시킨다.
 * */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter{
	
	
	@Autowired
	private PrincipalOauth2UserService principalOauth2UserService;
	
	
	
	//@Bean 해당 메소드의 리턴되는 오브젝트를 IOC에 등록해준다.
	@Bean	
	public BCryptPasswordEncoder encodePwd() {
		return new BCryptPasswordEncoder();
	}
	
	
	/**
	 * oauth 인증되면 코드를 받고 이를 이용해 엑세스 토큰을 가져올 수 있는데 
	 * 그걸 이용해 사용자 프로필 정보를 가져와서 곧바로 회원가입 시키거나 아니면 추가 정보를 받고 가입 시킴
	 * 구글에서는 엑세스 토큰과 사용자 프로필 정보를 동시에 가져온다.
	 * */
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
				.defaultSuccessUrl("/") //로그인 성공 시 이동하는 페이지
				.and()
				.oauth2Login()
				.loginPage("/loginForm")
				.userInfoEndpoint()
				.userService(principalOauth2UserService);
		
		
	}
}
