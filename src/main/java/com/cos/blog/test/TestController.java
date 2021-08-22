package com.cos.blog.test;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

@RestController
public class TestController {

	
	//{id} 주소로 파라메터를 전달 받을 수 있음
	//http://localhost:8000/blog/dummy/user
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping("/dummy/user")
	public List<User> list() {
		
		return userRepository.findAll();
	}
}
