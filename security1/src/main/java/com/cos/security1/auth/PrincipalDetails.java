package com.cos.security1.auth;

import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.cos.security1.model.User;

//시큐리티가 login페이지 요청을 낚아채서 로그인을 진행하는데
// 진행 완료되면 로그인이 성공 했다는 뜻으로 시큐리티가 관리하는 세션에 저장해준다.
// Security ContextHolder라는 Key?
// 시큐리티가 관리하는 세션에는 정해진 오브젝트만 들어갈 수 있다. Authentication 타입 객체만 가능함.
//Authentication 객체 안에는 User정보가 있어야 한다.
//User 오브젝트의 타입은 UserDetails 타입의 객체여야 한다.
// 즉, Security Session 에는 Authentication 객체여야 하고
// UserDetails 타입이어야 한다.
public class PrincipalDetails implements UserDetails{

	private User user;//콤포지션
	
	public PrincipalDetails(User user) {
		this.user = user;
	}
	
	
	// User의 권한을 리턴한다.
	// 이것은 user.getRole()하여 리턴하면 되는데 getRole() 함수는 String 타입이고
	// 이 함수는 GrantedAuthority를 상속 받는 Collection 타입으로 바꿔야 한다. 시큐리티에서 그렇게 정해놨기 때문,
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		//user.getRole()을 이 함수의 return 타입과 일치시킴		
		Collection<GrantedAuthority> collect = new ArrayList<>();
		collect.add(new GrantedAuthority() {
			@Override
			public String getAuthority() {				
				return user.getRole();
			}			
		});
		
		return collect;
	}

	
	@Override
	public String getPassword() {
		
		return user.getPassword();
	}

	
	@Override
	public String getUsername() {
		
		return user.getUsername();
	}

	//계정의 자격 만료 여부 
	@Override
	public boolean isAccountNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}
	
	
	// 계정의 잠김 여부
	@Override
	public boolean isAccountNonLocked() {
		// TODO Auto-generated method stub
		return true;
	}

	// 계정의 비밀번호 바꾼지 너무 오래되지 않았는가하는 여부
	@Override
	public boolean isCredentialsNonExpired() {
		// TODO Auto-generated method stub
		return true;
	}

	//계정 활성화 여부
	@Override
	public boolean isEnabled() {

		// 1년간 유저가 로그인 안하면 
		return true;
	}
	
	
}
