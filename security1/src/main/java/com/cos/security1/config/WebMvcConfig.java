package com.cos.security1.config;



import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.boot.web.servlet.view.MustacheViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;

//Mustache 쓸 때, 파일 끝에 .mustache 붙여야 하는 걸 생략 가능하게 해준다.
@Configuration
public class WebMvcConfig implements WebMvcConfigurer{
	
	
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {	
		MustacheViewResolver resolver = new MustacheViewResolver();
		resolver.setCharset("UTF-8");
		resolver.setContentType("text/html;charset=UTF-8");
		resolver.setPrefix("classpath:/templates/");

		resolver.setSuffix(".html");
		
		//뷰 리졸버 등록함
		registry.viewResolver(resolver);
		

		
	}
}
