package com.cos.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.Board;
import com.cos.blog.model.User;


//dao랑 같은의미
//자동으로 bean으로 등록이된다.
//@Repository 생략가능
public interface BoardRepository extends JpaRepository<Board, Integer> {

	
	
	//JPA Namining 쿼리 
	//select * from user where username =?1 and password = ?2;
	//User findByUsernameAndPassword(String username, String password);
	
	//@Query(value="SELECT * FROM user WHERE username =?1 AND password =?2", nativeQuery =true)
	//User login(String username,String password);
	
	
	
}
