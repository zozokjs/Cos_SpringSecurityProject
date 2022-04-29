package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class IndexController {
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
	@GetMapping("/login")
	public @ResponseBody String login() {
		return "login";
	}
	
	@GetMapping("/join")
	public @ResponseBody String join() {
		return "join";
	}
	
	@GetMapping("/joinProc")
	public @ResponseBody String joinProc() {
		return "회원가입 완려됨";
	}
	
	
}
