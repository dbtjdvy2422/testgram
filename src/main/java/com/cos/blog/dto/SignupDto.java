package com.cos.blog.dto;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import com.cos.blog.model.User;

import lombok.Data;

@Data // Getter, Setter
public class SignupDto {
	// https://bamdule.tistory.com/35 (@Valid 어노테이션 종류)
	@Size(min = 2, max = 20)
	@NotBlank
	private String username;
	@NotBlank
	private String password;
	@NotBlank
	private String email;
	
	public User toEntity() {
		return User.builder()
				.username(username)
				.password(password)
				.email(email)
				.build();
	}
}
