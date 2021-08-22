package com.cos.blog.controller.api;

import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticatedPrincipal;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.dto.CMRespDto;
import com.cos.blog.dto.ResponseDto;
import com.cos.blog.dto.SignupDto;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

import lombok.RequiredArgsConstructor;



@RequiredArgsConstructor
@RestController
public class UserApiController {

	
	private final UserService userService;
	private final AuthenticationManager authenticationManager;
	

	/*
	@PostMapping("/api/user/login")
	public ResponseDto<Integer> login(@RequestBody User user,HttpSession session) {
		System.out.println("UserApiController :login 호줄됨");
		User principal = userService.로그인(user); //principal (접근추체)
		
		if(principal!= null) {
			session.setAttribute("pincipal", principal);
		}
		return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
	}
	*/
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user,
			@AuthenticationPrincipal PrincipalDetail principal, HttpSession session){
		userService.회원수정(user);

		
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return new ResponseDto<Integer>(HttpStatus.OK.value(),1);
	}
	
	@PutMapping("/api/user/{principalId}/profileImageUrl")
	public ResponseEntity<?> profileImageUrlUpdate(@PathVariable int principalId, MultipartFile profileImageFile, 
			@AuthenticationPrincipal PrincipalDetail principalDetail){
		User userEntity = userService.회원프로필사진변경(principalId, profileImageFile);
		principalDetail.setUser(userEntity); // 세션 변경
		return new ResponseEntity<>(new CMRespDto<>(1, "프로필사진변경 성공", null), HttpStatus.OK);
	}
	
	
	
}
