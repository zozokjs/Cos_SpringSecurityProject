package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller
public class IndexController {
	
	
	@Autowired	
	UserRepository userRepository;
	
	@Autowired	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping("/test/login")
	public @ResponseBody String testLogin
	(Authentication authentication, @AuthenticationPrincipal PrincipalDetails userDetails) {
		
		System.out.println("/test/login -------------");	
		
		PrincipalDetails principalDetails = (PrincipalDetails)authentication.getPrincipal();
		System.out.println("authentication "+principalDetails.getUser());
		System.out.println("userDetail "+userDetails.getUser());
		//둘 다 같은 정보를 가져 옴(일반 계정으로 로그인 할 떄)
		return "세션 정보 확인 ";
		
	}
	
	
	@GetMapping("/test/oauth/login")
	public @ResponseBody String testOAuthLogin
	(Authentication authentication, @AuthenticationPrincipal OAuth2User oauth ) {
		
		System.out.println("/test/oauth/login -------------");
		
		OAuth2User oAuth2User = (OAuth2User)authentication.getPrincipal();
		System.out.println("authentication "+oAuth2User.getAttributes());
		System.out.println("OAuth2User "+oauth.getAttributes());
		//둘 다 같은 정보를 가져 옴( 구글 계정으로 로그인 할 떄)
		
		return "OAuth 세션 정보 확인 ";
		
	}
	
	@GetMapping({ "","/" })
	public String index() {
		
		//mustache 머스테치 - html 경로 잡아줄 때 양식을 간단하게 만듬
		//기본 폴더 : src/main/resources
		//접두사(prefix) : templates, 접미사(suffix) : .mustache인데 생략 가능함
		return "index";
	}
	
	
	@GetMapping("/user")
	public @ResponseBody String user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
		System.out.println("principalDetails "+principalDetails.getUser());
		return "user";
	}

	
	@GetMapping("/admin")
	public @ResponseBody String admin() {
		return "admin";
	}
	
	
	@GetMapping("/manager")
	public @ResponseBody String manager() {
		return "manager";
	}
	
	
	//본래 아래 주소로 들어가면 시큐리티에서 기본 제공하는 로그인 페이지가 표시되는데,
	//SecurityConfig 파일 생성 후에는 그렇게 안 됨.
	@GetMapping("/loginForm")
	public  String loginForm() {
		return "loginForm";
	}
	
	@PostMapping("/join")
	public  String join(User user) {
		System.out.println(user);
		user.setRole("ROLE_USER");
		//UserRepository는 JpaRepository를 상속 받았기에 자동으로 IOC 적용되고, CRUD 함수 적용됨.
		//이로써 회원가입은 가능하나, 시큐리티로 로그인 불가. 비밀번호 페스워드 암호화가 필요함.
		
		//비번 받고 암호화 처리
		String rawPassword = user.getPassword();
		String encPassword = bCryptPasswordEncoder.encode(rawPassword);
		user.setPassword(encPassword);
		
		
		//save 처리
		userRepository.save(user); 		
		
		return "redirect:/loginForm";
	}
	
	@GetMapping("/joinForm")
	public String joinForm() {
		return "joinForm";
	}

	
	/**
	 * 아래 함수는 admin만 접근 가능
	 * 
	 * */
	@Secured("ROLE_ADMIN")
	@GetMapping("/info")
	public @ResponseBody String info() {
		return "개인정보";
	}
	
	
	/**PreAuthorize는 해당 함수 실행 직전에 실행된다. 함수 실행 직후에 실행되게 하려면 @PostAuthrize를 쓴다.
	 *그리고 WebSecurityConfigurerAdapter를 상속 받은 클래스에서 @EnableGlobalMethodSecurity를 걸어줘야 한다.
	 *
	 * hasRole('ROLE_~~~')은 정해진 양식임
	 * 아래 함수는 manager와 admin 권한만 접근 가능
	 * */
	@PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')")
	@GetMapping("/data")
	public @ResponseBody String data() {
		return "데이터정보";
	}
	
	
	
}
