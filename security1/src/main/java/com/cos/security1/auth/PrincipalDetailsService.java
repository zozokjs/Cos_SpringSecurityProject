package com.cos.security1.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

/**
	시큐리티 설정(SercurityConfig.java)에서
	loginProcessingUrl("/login") 이렇게 했기 때문에
	/login 페이지 요청이 오면 UserDetailsService 타입으로 IOC 되어 있는
	loadUserByUsername 메서드가 자동으로 실행되도록 되어 있다.
 *  */
@Service
public class PrincipalDetailsService implements UserDetailsService{

	/**
	 *  위의 주석에서 /login 페이지로 요청 오면 아래의 메서드가 자동 실행된다고 했는데,
	 *  이 메서드에서 매개변수로 받는 username은 
	 *  /login 페이지가 요청되는 로그인 페이지(이 프로젝트에선 templates 폴더 안에 있는 loginForm.html)에 있는
	 *  name과 반드시 일치해야 한다. 예를 들어 <input type= "text" name = "username2"> 이렇게 하면 못받음.
	 *  username2로 받고 싶다면
	 *  SecurityConfig클래스에 구현 해 놓은 configure() 메소드에 아래 옵션을 추가해야 함.
	 *     	http.authorizeRequests()....usernameParameter("username2")
	 *     
	 *     * */
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		// 로그인 페이지 요청으로 username이 왔으니 이 유저가 실제로 존재하는지 확인해야 함.
		User userEntity = userRepository.findByUsername(username);
		System.out.println("username "+username);
		//userEntity가 존재한다면 로그인 성공
		if(userEntity != null) {
			
			/** security session에는 Authentication 타입만 들어가고
			   Authentication 타입에는 UserDetails 타입만 들어가는데
			  	return new PrincipalDetails(userEntity); 
			  	여기서 PrincipalDetails는 UserDetails를 상속 받기 때문에 return이 정상적으로 이뤄지고
			  	그 결과 UserDetails타입의 유저 정보가 Authetication 객체에 들어간다.
			  	그 결과, security session에 Authetication 객체가 들어간다.	  	
			 * */
			return new PrincipalDetails(userEntity);
		}
		
		return null;
	}
	

	
	
	
}
