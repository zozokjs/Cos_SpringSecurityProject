package com.cos.security1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
	@GetMapping({ "","/" })
	public String index() {
		
		//mustache 머스테치 - html 경로 잡아줄 때 양식을 간단하게 만듬
		//기본 폴더 : src/main/resources
		//접두사(prefix) : templates, 접미사(suffix) : .mustache인데 생략 가능함
		return "index";
	}
}
