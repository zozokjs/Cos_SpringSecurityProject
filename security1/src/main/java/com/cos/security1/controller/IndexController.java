package com.cos.security1.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Controller
public class IndexController {
	
	
	@Autowired	
	UserRepository userRepository;
	
	@Autowired	
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@GetMapping({ "","/" })
	public String index() {
		
		//mustache 머스테치 - html 경로 잡아줄 때 양식을 간단하게 만듬
		//기본 폴더 : src/main/resources
		//접두사(prefix) : templates, 접미사(suffix) : .mustache인데 생략 가능함
		return "index";
	}
	
	
	@GetMapping("/user")
	public @ResponseBody String user() {
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

	
	
}
