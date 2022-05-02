package com.cos.security1.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.cos.security1.auth.PrincipalDetails;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService{

	//@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;


	public PrincipalOauth2UserService(BCryptPasswordEncoder b) {
		this.bCryptPasswordEncoder = b;
	}
	
	@Autowired	
	private UserRepository userRepository;
	
	
	//구글로부터 받은 userRequest 데이터에 대한 후처리 함수
	@Override
	public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
	
		System.out.println("getClientRegistration " +userRequest.getClientRegistration());
		System.out.println("getTokenValue " +userRequest.getAccessToken().getTokenValue());
		
		
		/**
		 * 구글 로그인 버튼 클릭 > 구글 로그인 창 > 로그인 완료 > code 리턴 됨(OAuth Client 라이브러리가 받아 줌) 
		 *  -> AcceeToken 요청 됨. 여기까지하면 userRequest 정보가 온다.
		 *  
		 *  이 정보에서 loadUser()를 이용해 회원 프로필을 받을 수 있음.
		 * */
		OAuth2User oAuth2User = super.loadUser(userRequest);
		System.out.println("getAttributes " + oAuth2User.getAttributes());
		
		
		String provider = userRequest.getClientRegistration().getClientId();//google
		String providerId = oAuth2User.getAttribute("sub");
		String email = oAuth2User.getAttribute("email");
		String username = provider+"_"+providerId;//google_12314123
		String password = bCryptPasswordEncoder.encode("비비");
		//String password = "아무거나가능";
		String role = "ROLE_USER";
		
		//회원가입 되어 있는지 체크
		User userEntity = userRepository.findByUsername(username);
		
		if(userEntity == null) {
			
			userEntity = User.builder()
					.username(username)
					.password(password)
					.email(email)
					.role(role)
					.provider(providerId)
					.providerId(providerId)
					.build();
			userRepository.save(userEntity);			
		}
		
		//이렇게 리턴 되면서 Authentication 객체에 들어감
		return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
	}
}
