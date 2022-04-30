package com.cos.security1.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cos.security1.model.User;

//JpaRepository가 CRUD 함수를  기본 함수를 갖고 있음
// @Repository가 없어도 IOC 적용 됨. JpaRepository를 상속 했기 때문임. 
public interface UserRepository extends JpaRepository<User, Integer>{

	/** 
	//findBy...까지는 규칙이고
	// Username이라 적는 건 문법임.
	//이 규칙과 문법을 지키면
	// [  select * from user where username = 1?  ] 이 자동 호출됨
	 * 
	 *  같은 예로, public User findByEmail()함수를 만들면
	 *  [  select * from user where email = 1?  ] 이 자동 호출됨
	 *  
	 *  'JPA 쿼리 메서드' 참조
	 * */
	public User findByUsername(String username);
	
	
}
